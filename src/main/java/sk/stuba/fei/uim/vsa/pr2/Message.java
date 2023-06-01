package sk.stuba.fei.uim.vsa.pr2;

import lombok.*;

@Data
public class Message {
    private int code;
    private String message;
    private Error error;

    public Message(int code, String message,  Error error){
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public Message() {
    }
}
