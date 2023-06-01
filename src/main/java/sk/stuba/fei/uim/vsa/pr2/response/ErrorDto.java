package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDto {
    private String type;
    private String trace;
}
