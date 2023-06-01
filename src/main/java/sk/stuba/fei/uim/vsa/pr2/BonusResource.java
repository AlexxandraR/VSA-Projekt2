package sk.stuba.fei.uim.vsa.pr2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.bonus.Pageable1;
import sk.stuba.fei.uim.vsa.pr2.response.StudentBonus;
import sk.stuba.fei.uim.vsa.jpaService.Service;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_STUDENT;
import static sk.stuba.fei.uim.vsa.pr2.auth.Permission.PERM_TEACHER;
import sk.stuba.fei.uim.vsa.pr2.auth.Secured;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogBonus;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogPageDto;
import sk.stuba.fei.uim.vsa.pr2.response.StudentPageDto;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaBonus;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaPageDto;
import sk.stuba.fei.uim.vsa.pr2.response.factory.PedagogPageFactory;
import sk.stuba.fei.uim.vsa.pr2.response.factory.StudentPageFactory;
import sk.stuba.fei.uim.vsa.pr2.response.factory.ZaverecnaPracaPageFactory;

@Slf4j
@Path("/search")
public class BonusResource {
    private final Service service = Service.getInstance();
    private final ObjectMapper json = new ObjectMapper();
    
    @POST
    @Path("/students")
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchStudent(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("20") int size, StudentBonus student) throws JsonProcessingException {

        String name = student.getName();
        String year = student.getYear();

        Pageable1 pa = new Pageable1(page, size);
        Page1 p = (Page1)service.findStudents(Optional.ofNullable(name), Optional.ofNullable(year), pa);

        StudentPageFactory factory = new StudentPageFactory();
        
        StudentPageDto spdto = factory.transformToDto(p);
        String jsonFile = json.writeValueAsString(spdto);
        
        return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
    
    @POST
    @Path("/teachers")
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchPedagog(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("20") int size, PedagogBonus pedagog) throws JsonProcessingException {

        String name = pedagog.getName();
        String institute = pedagog.getInstitute();

        Pageable1 pa = new Pageable1(page, size);
        Page1 p = (Page1)service.findTeachers(Optional.ofNullable(name), Optional.ofNullable(institute), pa);

        PedagogPageFactory factory = new PedagogPageFactory();
        
        PedagogPageDto spdto = factory.transformToDto(p);
        String jsonFile = json.writeValueAsString(spdto);
        
        return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
    
    @POST
    @Path("/theses")
    @Secured({PERM_STUDENT, PERM_TEACHER})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchTheses(@QueryParam("page") @DefaultValue("0") int page, @QueryParam("size") @DefaultValue("20") int size, ZaverecnaPracaBonus zv) throws JsonProcessingException, ParseException {

        Long studentId = zv.getStudentId();
        Long teacherId = zv.getTeacherId();
        String department = zv.getDepartment();
        
        Date publishedOn = null;
        
        if(zv.getPublishedOn() != null){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date publishdate = dateFormat.parse(zv.getPublishedOn());

            publishedOn = publishdate;
        }
        
        String type = zv.getType();
        String status = zv.getStatus();

        Pageable1 pa = new Pageable1(page, size);
        
        Page1 p = (Page1)service.findTheses(Optional.ofNullable(studentId), Optional.ofNullable(teacherId), Optional.ofNullable(department), Optional.ofNullable(publishedOn), Optional.ofNullable(type), Optional.ofNullable(status), pa);

        ZaverecnaPracaPageFactory factory = new ZaverecnaPracaPageFactory();
        
        ZaverecnaPracaPageDto zvpdto = factory.transformToDto(p);
        String jsonFile = json.writeValueAsString(zvpdto);
        
        return Response
                .status(Response.Status.OK)
                .entity(jsonFile)
                .build();
    }
}
