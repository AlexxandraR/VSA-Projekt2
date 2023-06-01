package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDto {
    private int code;
    private String message;
    private ErrorDto error;
}
