package sk.stuba.fei.uim.vsa.pr2.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PedagogPageDto {
    private List<PedagogDto> content;
    private PageDto page;
}
