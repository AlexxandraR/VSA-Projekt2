package sk.stuba.fei.uim.vsa.pr2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.entities.RequestBody;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.entities.ZaverecnaPraca;
import sk.stuba.fei.uim.vsa.jpaService.Service;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_STUDENT;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_TEACHER;
import sk.stuba.fei.uim.vsa.pr2.auth.Secured;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaDto;
import sk.stuba.fei.uim.vsa.pr2.response.factory.ZaverecnaPracaFactory;

@Path("/theses")
public class ZaverecnaPracaResource {
    @Context
    SecurityContext securityContext;
    private final Service service = Service.getInstance();
    private final ZaverecnaPracaFactory factory = new ZaverecnaPracaFactory();
    
    private final ObjectMapper json = new ObjectMapper();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({PERM_STUDENT, PERM_TEACHER})
    public Response getZaverecnaPraca() throws JsonProcessingException {
        List<ZaverecnaPraca> zv = service.getTheses();
        if(zv == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        List<ZaverecnaPracaDto> zvdto = new ArrayList();
        for(ZaverecnaPraca zv1 : zv){
            zvdto.add(factory.transformToDto(zv1));
        }
        String jsonFile = json.writeValueAsString(zvdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
            
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({PERM_TEACHER})
    public Response createZaverecnaPraca(ZaverecnaPraca zv) throws JsonProcessingException {
        ZaverecnaPraca zv1 = service.makeThesisAssignment(((Pedagog) securityContext.getUserPrincipal()).getAisId(), zv.getRegistrationNumber(), zv.getTitle(), zv.getType().toString(), zv.getDescription());
        if(zv1 == null){
            throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
        }
        ZaverecnaPracaDto zvdto = factory.transformToDto(zv1);
        String jsonFile = json.writeValueAsString(zvdto);
        return Response
                .status(Response.Status.CREATED)
                .entity(jsonFile)
                .build();
        
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({PERM_STUDENT, PERM_TEACHER})
    public Response getZaverecnaPraca(@PathParam("id") Long id) throws JsonProcessingException {
        ZaverecnaPraca zv = service.getThesis(id);
        if(zv == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        ZaverecnaPracaDto zdto = factory.transformToDto(zv);
        String jsonFile = json.writeValueAsString(zdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
    
    private boolean hasNoAuthorization(Long id) {
        try{
            if(!Objects.equals(((Pedagog) securityContext.getUserPrincipal()).getAisId(), id)){
                return true;
            }
            return false;
        }catch(ClassCastException e){
            return true;
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Secured({PERM_TEACHER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTheses(@PathParam("id") Long id) throws JsonProcessingException {
        ZaverecnaPraca zv = service.getThesis(id);
        if(zv == null){
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        if(hasNoAuthorization(zv.getSupervisor().getAisId())){
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        zv = service.deleteThesis(id);
        if(zv == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        ZaverecnaPracaDto zvdto = factory.transformToDto(zv);
        String jsonFile = json.writeValueAsString(zvdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
    
     private boolean isPedagog() {
        try{
            Pedagog p = (Pedagog) securityContext.getUserPrincipal();
            return true;
        }catch(ClassCastException e){
            return false;
        }
    }
    
    @POST
    @Path("/{id}/assign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({PERM_TEACHER, PERM_STUDENT})
    public Response assignZaverecnaPraca(@PathParam("id") Long id, @Valid RequestBody requestBody) throws JsonProcessingException {
        ZaverecnaPraca zv1 = null;
        if(isPedagog()){
            if(requestBody == null){
               throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
            }
            zv1 = service.assignThesis(id, requestBody.getStudentId());
        }
        else if(!isPedagog()){
            zv1 = service.assignThesis(id, ((Student) securityContext.getUserPrincipal()).getAisId());
        }
        else{
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        if(zv1 == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        ZaverecnaPracaDto zvdto = factory.transformToDto(zv1);
        String jsonFile = json.writeValueAsString(zvdto);
        return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
    
    @POST
    @Path("/{id}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured({PERM_TEACHER, PERM_STUDENT})
    public Response submitZaverecnaPraca(@PathParam("id") Long id, @Valid RequestBody requestBody) throws JsonProcessingException {
        ZaverecnaPraca zv1 = service.getThesis(id);
        if(zv1 == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        if(isPedagog()){
            if(requestBody == null){
               throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
            }
            if(service.getStudent(requestBody.getStudentId()) == null){
                throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
            }
            if(Objects.equals(requestBody.getStudentId(), service.getThesis(id).getAuthor().getAisId())){
                zv1 = service.submitThesis(id);
            }
            else{
                throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
            }
        }
        else if(!isPedagog()){
            if(Objects.equals(((Student) securityContext.getUserPrincipal()).getAisId(), service.getThesis(id).getAuthor().getAisId())){
                zv1 = service.submitThesis(id);
            }
            else{
                throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
            }
        }
        else{
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        if(zv1 == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        ZaverecnaPracaDto zvdto = factory.transformToDto(zv1);
        String jsonFile = json.writeValueAsString(zvdto);
        return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
}
