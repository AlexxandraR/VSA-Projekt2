package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.*;
import sk.stuba.fei.uim.vsa.entities.Status;
import sk.stuba.fei.uim.vsa.entities.Typ;

@Data
@NoArgsConstructor
public class ZaverecnaPracaDto {
    private Long id;
    private String registrationNumber;
    private String title;
    private String description;
    private String department;
    private String publishedOn;
    private String deadline;
    private Typ type;
    private Status status;
    private ShorterPedagogDto supervisor;
    private ShorterStudentDto author;
}
