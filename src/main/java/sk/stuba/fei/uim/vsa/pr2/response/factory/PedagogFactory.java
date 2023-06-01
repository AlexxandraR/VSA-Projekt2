package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.util.stream.Collectors;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogDto;

public class PedagogFactory implements ResponseFactory<Pedagog, PedagogDto>{
    
    @Override
    public PedagogDto transformToDto(Pedagog entity) {
        PedagogDto dto = new PedagogDto();
        
        dto.setId(entity.getAisId());
        dto.setAisId(entity.getAisId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setDepartment(entity.getDepartment());
        dto.setInstitute(entity.getInstitute());
        ZaverecnaPracaFactory factory = new ZaverecnaPracaFactory();
        dto.setTheses(entity.getTheses().stream().map(factory::transformToDto).collect(Collectors.toList()));
        return dto;
    }
    
}
