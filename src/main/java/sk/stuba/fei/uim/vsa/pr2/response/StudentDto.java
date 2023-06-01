package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.*;

@Data
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private Long aisId;
    private String email;
    private String name;
    private String programme;
    private Integer year;
    private Integer term;
    private ZaverecnaPracaDto thesis;
}
