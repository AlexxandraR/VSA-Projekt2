package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.*;

@Data
@NoArgsConstructor
public class ZaverecnaPracaBonus {
    private Long studentId;
    private Long teacherId;
    private String department;
    private String publishedOn;
    private String type;
    private String status;
}
