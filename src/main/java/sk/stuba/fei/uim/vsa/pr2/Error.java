package sk.stuba.fei.uim.vsa.pr2;

import lombok.Data;

@Data
public class Error{
    
    private String type;
    private String trace;

    public Error() {
    }
    
    public Error(String type, StackTraceElement[] trace){
        this.type = type;
        this.trace = serializeStackTrace(trace);
    }
    
    private static String serializeStackTrace(StackTraceElement[] trace){
        StringBuilder builder = new StringBuilder(trace.length);
        for(StackTraceElement s : trace){
            builder.append("\t");
            builder.append(s.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
