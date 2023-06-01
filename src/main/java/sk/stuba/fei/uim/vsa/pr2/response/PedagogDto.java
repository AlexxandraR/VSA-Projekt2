package sk.stuba.fei.uim.vsa.pr2.response;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
public class PedagogDto {
    private Long id;
    private Long aisId;
    private String email;
    private String name;
    private String institute;
    private String department;
    private List<ZaverecnaPracaDto> theses;

}
