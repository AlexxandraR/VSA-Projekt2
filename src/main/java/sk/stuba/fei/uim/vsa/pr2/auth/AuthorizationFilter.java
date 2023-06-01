package sk.stuba.fei.uim.vsa.pr2.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.pr2.Message;
import sk.stuba.fei.uim.vsa.pr2.Error;

@Slf4j
@Provider
@Secured
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter{

    @Context
    private ResourceInfo resourceInfo;
    
    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        Student student = null;
        Pedagog pedagog = null;
        if(request.getSecurityContext() instanceof BasicStudentSecurityContext){
            student = (Student) request.getSecurityContext().getUserPrincipal();
            student.addPermission(Permission.PERM_STUDENT);
        }
        else{
            pedagog = (Pedagog) request.getSecurityContext().getUserPrincipal();
            pedagog.addPermission(Permission.PERM_TEACHER);
        }
        
        Method resourceMethod = resourceInfo.getResourceMethod();
        Set<Permission> permissions = extractPermissionsFromMethon(resourceMethod);
                
        if((student != null && !permissions.contains(student.getPermission())) || (pedagog != null && !permissions.contains(pedagog.getPermission()))){
            request.abortWith(Response
                    .status(Status.FORBIDDEN)
                    .entity(new Message(Response.Status.FORBIDDEN.getStatusCode(), "You are not authorized enough.", new Error("errorMessage", new Throwable().getStackTrace())))
                    .build());
        }
    }
    
    private Set<Permission> extractPermissionsFromMethon(Method method){
        if(method == null){
            return new HashSet<>();
        }
        Secured secured = method.getAnnotation(Secured.class);
        if(secured == null){
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(secured.value()));
    }
    
}
