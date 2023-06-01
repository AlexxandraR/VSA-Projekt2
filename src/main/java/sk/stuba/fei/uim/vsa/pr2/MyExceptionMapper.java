package sk.stuba.fei.uim.vsa.pr2;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyExceptionMapper implements ExceptionMapper<Throwable>{

    @Override
    public Response toResponse(Throwable e) {
        if (e instanceof WebApplicationException) {
            WebApplicationException webAppException = (WebApplicationException) e;
            int statusCode = webAppException.getResponse().getStatus();
            if(statusCode == 401){
                return Response.status(401).type(MediaType.APPLICATION_JSON).entity(new Message(401, "You are not an authenticated user.", new Error(e.getClass().getName(), e.getStackTrace()))).build();
            }
            else if(statusCode == 403){
                return Response.status(403).type(MediaType.APPLICATION_JSON).entity(new Message(403, "You are not authorized enough.", new Error(e.getClass().getName(), e.getStackTrace()))).build();
            }
            else if(statusCode == 404){
                return Response.status(404).type(MediaType.APPLICATION_JSON).entity(new Message(404, "The server has not found anything matching the Request-URI.", new Error(e.getClass().getName(), e.getStackTrace()))).build();
            }
            else{
                return Response.status(500).type(MediaType.APPLICATION_JSON).entity(new Message(500, "The server encountered an unexpected condition which prevented it from fulfilling the request.", new Error(e.getClass().getName(), e.getStackTrace()))).build();
            }
        }
        else{
          return Response.status(500).type(MediaType.APPLICATION_JSON).entity(new Message(500, "The server encountered an unexpected condition which prevented it from fulfilling the request.", new Error(e.getClass().getName(), e.getStackTrace()))).build();
        }
    }
    
}
