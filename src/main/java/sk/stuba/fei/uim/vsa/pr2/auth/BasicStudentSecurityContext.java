package sk.stuba.fei.uim.vsa.pr2.auth;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
import sk.stuba.fei.uim.vsa.entities.Student;

public class BasicStudentSecurityContext implements SecurityContext{

    private final Student student;
    private boolean secure;

    public BasicStudentSecurityContext(Student student) {
        this.student = student;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return student;
    }

    @Override
    public boolean isUserInRole(String string) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Basic";
    }
    
}
