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
import lombok.extern.slf4j.Slf4j;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.jpaService.Service;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.*;
import sk.stuba.fei.uim.vsa.pr2.auth.Secured;
import sk.stuba.fei.uim.vsa.pr2.response.StudentDto;
import sk.stuba.fei.uim.vsa.pr2.response.factory.StudentFactory;

@Slf4j
@Path("/students")
public class StudentResource {
    private final Service service = Service.getInstance();
    private final StudentFactory factory = new StudentFactory();
    
    private final ObjectMapper json = new ObjectMapper();
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents() throws JsonProcessingException {
        List<Student> students = service.getStudents();
            if(students == null){
                throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
            }
            List<StudentDto> sdto = new ArrayList();
            for(Student s : students){
                sdto.add(factory.transformToDto(s));
            }
            String jsonFile = json.writeValueAsString(sdto);
            return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStudent(Student student) throws JsonProcessingException {
        String password = new String(Base64.getDecoder().decode(student.getPassword()));
        Student s = service.createStudent(student.getAisId(), student.getName(), student.getEmail(), BCryptService.hash(password));
        if(s == null){
            throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
        }
        s = service.updateStudent(student);
        if(s == null){
            throw new MyException("The server encountered an unexpected condition which prevented it from fulfilling the request.", new Throwable(), Response.Status.INTERNAL_SERVER_ERROR, "errorMessage");
        }
        StudentDto sdto = factory.transformToDto(s);
        String jsonFile = json.writeValueAsString(sdto);
        return Response
                .status(Response.Status.CREATED)
                .entity(jsonFile)
                .build();
    }
    
    @GET
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentById(@PathParam("id") Long id) throws JsonProcessingException {
        Student student = service.getStudent(id);
        if(student == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        StudentDto sdto = factory.transformToDto(student);
        String jsonFile = json.writeValueAsString(sdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
    
    private boolean hasNoAuthorization(Long id) {
        try{
            if(!Objects.equals(((Student) securityContext.getUserPrincipal()).getAisId(), id)){
                return true;
            }
            return false;
        }catch(ClassCastException e){
            return false;
        }
    }
    
    @DELETE
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") Long id) throws JsonProcessingException {
        if(hasNoAuthorization(id)){
            throw new MyException("You are not authorized enough.", new Throwable(), Response.Status.FORBIDDEN, "errorMessage");
        }
        Student student = service.deleteStudent(id);
        if(student == null){
            throw new MyException("The server has not found anything matching the Request-URI.", new Throwable(), Response.Status.NOT_FOUND, "errorMessage");
        }
        StudentDto sdto = factory.transformToDto(student);
        String jsonFile = json.writeValueAsString(sdto);
        return Response
            .status(Response.Status.OK)
            .entity(jsonFile)
            .build();
    }
}
