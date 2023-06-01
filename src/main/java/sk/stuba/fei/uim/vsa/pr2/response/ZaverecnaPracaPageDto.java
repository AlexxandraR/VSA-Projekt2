package sk.stuba.fei.uim.vsa.pr2.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZaverecnaPracaPageDto {
    private List<ZaverecnaPracaDto> content;
    private PageDto page;
}
