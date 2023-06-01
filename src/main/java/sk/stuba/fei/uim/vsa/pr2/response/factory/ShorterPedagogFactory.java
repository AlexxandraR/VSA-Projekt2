package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.util.ArrayList;
import java.util.List;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.entities.ZaverecnaPraca;
import sk.stuba.fei.uim.vsa.pr2.response.ShorterPedagogDto;

public class ShorterPedagogFactory implements ResponseFactory<Pedagog, ShorterPedagogDto>{
    @Override
    public ShorterPedagogDto transformToDto(Pedagog entity) {
        ShorterPedagogDto dto = new ShorterPedagogDto();
        
        dto.setId(entity.getAisId());
        dto.setAisId(entity.getAisId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setDepartment(entity.getDepartment());
        dto.setInstitute(entity.getInstitute());
        
        List<Long> zv = new ArrayList<>();
            for (ZaverecnaPraca z : entity.getTheses()){
                zv.add(z.getId());
            }
        dto.setTheses(zv);
        return dto;
    }
}
