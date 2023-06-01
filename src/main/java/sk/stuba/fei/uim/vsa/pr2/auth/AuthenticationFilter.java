package sk.stuba.fei.uim.vsa.pr2.auth;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.jpaService.Service;
import sk.stuba.fei.uim.vsa.pr2.BCryptService;
import sk.stuba.fei.uim.vsa.pr2.Message;
import sk.stuba.fei.uim.vsa.pr2.Error;

@Slf4j
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String authHeader = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if(authHeader == null || !authHeader.contains("Basic")){
            reject(request);
            return;
        }
        
        String[] credentials = null;
        
        try{
            credentials = extractFromAuthHeader(authHeader);
        }catch(Exception e){
            reject(request);
            return;
        }
        if(credentials.length < 2){
            reject(request);
            return;
        }
        
        Service service = Service.getInstance();
        Optional<Student> studentOptional = service.getStudentByEmail(credentials[0]);
        
        Optional<Pedagog> pedagogOptional = service.getTeacherByEmail(credentials[0]);
        
        if((!studentOptional.isPresent() || !BCryptService.verify(credentials[1], studentOptional.get().getPassword())) 
                && (!pedagogOptional.isPresent() || !BCryptService.verify(credentials[1], pedagogOptional.get().getPassword()))){
            reject(request);
            return;
        }
        
        final SecurityContext securityContext = request.getSecurityContext();
        
        if(studentOptional.isPresent()){
            BasicStudentSecurityContext context = new BasicStudentSecurityContext(studentOptional.get());
            context.setSecure(securityContext.isSecure());
            request.setSecurityContext(context);
        }
        else{
            BasicPedagogSecurityContext context = new BasicPedagogSecurityContext(pedagogOptional.get());
            context.setSecure(securityContext.isSecure());
            request.setSecurityContext(context);
        }
        
    }
    
    private void reject(ContainerRequestContext request){
        request.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm = \"VSA\"")
                .entity(new Message(Response.Status.UNAUTHORIZED.getStatusCode(), "You are not an authenticated user.", new Error("errorMessage", new Throwable().getStackTrace())))
                .build());
    }
    
    private String[] extractFromAuthHeader(String authHeader){
        return new String(Base64.getDecoder().decode(authHeader.replace("Basic", "").trim())).split(":");
    }
}
