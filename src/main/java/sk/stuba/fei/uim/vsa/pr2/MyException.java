package sk.stuba.fei.uim.vsa.pr2;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyException extends WebApplicationException{

    public MyException(String message, Throwable cause, Response.Status status, String type) {
        super(Response.status(status).type(MediaType.APPLICATION_JSON).entity(new Message(status.getStatusCode(), message, new Error(type, cause.getStackTrace()))).build());
    }

    
}
