package sk.stuba.fei.uim.vsa.pr2.auth;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
import sk.stuba.fei.uim.vsa.entities.Pedagog;

public class BasicPedagogSecurityContext implements SecurityContext{
    
    private final Pedagog pedagog;
    private boolean secure;

    public BasicPedagogSecurityContext(Pedagog pedagog) {
        this.pedagog = pedagog;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return pedagog;
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
