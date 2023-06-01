package sk.stuba.fei.uim.vsa.pr2.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShorterPedagogDto {
    private Long id;
    private Long aisId;
    private String email;
    private String name;
    private String institute;
    private String department;
    private List<Long> theses;
}
