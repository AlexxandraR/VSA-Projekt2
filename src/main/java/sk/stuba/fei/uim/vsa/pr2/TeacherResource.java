package sk.stuba.fei.uim.vsa.pr2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.jpaService.Service;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_STUDENT;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_TEACHER;
import sk.stuba.fei.uim.vsa.pr2.auth.Secured;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogDto;
import sk.stuba.fei.uim.vsa.pr2.response.factory.PedagogFactory;

@Path("/teachers")
public class TeacherResource {
    @Context
    SecurityContext securityContext;
    private final Service service = Service.getInstance();
    private final PedagogFactory factory = new PedagogFactory();
    
    private final ObjectMapper json = new ObjectMapper();
    
    @GET
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeachers() throws JsonProcessingException {
        List<Pedagog> teachers = service.getTeachers();
        if(teachers == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        List<PedagogDto> pdto = new ArrayList();
        for(Pedagog p : teachers){
            pdto.add(factory.transformToDto(p));
        }
        String jsonFile = json.writeValueAsString(pdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTeacher(Pedagog teacher) throws JsonProcessingException {
        String password = new String(Base64.getDecoder().decode(teacher.getPassword()));
        Pedagog t = service.createTeacher(teacher.getAisId(), teacher.getName(), teacher.getEmail(), teacher.getDepartment(), BCryptService.hash(password));
        if(t == null){
            throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
        }
        t = service.updateTeacher(teacher);
        if(t == null){
            throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
        }
        PedagogDto pdto = factory.transformToDto(t);
        String jsonFile = json.writeValueAsString(pdto);
        return Response
                .status(Response.Status.CREATED)
                .entity(jsonFile)
                .build();
        
    }
    
    @GET
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(@PathParam("id") Long id) throws JsonProcessingException {
        Pedagog teacher = service.getTeacher(id);
        if(teacher == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        PedagogDto pdto = factory.transformToDto(teacher);
        String jsonFile = json.writeValueAsString(pdto);
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
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTeacher(@PathParam("id") Long id) throws JsonProcessingException {
        if(hasNoAuthorization(id)){
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        Pedagog teacher = service.deleteTeacher(id);
        if(teacher == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        PedagogDto pdto = factory.transformToDto(teacher);
        String jsonFile = json.writeValueAsString(pdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
}
