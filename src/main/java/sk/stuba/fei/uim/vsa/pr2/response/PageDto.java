package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageDto {
    private Integer number;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
