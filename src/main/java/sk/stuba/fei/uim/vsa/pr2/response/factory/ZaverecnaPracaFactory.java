package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.time.format.DateTimeFormatter;
import sk.stuba.fei.uim.vsa.entities.ZaverecnaPraca;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaDto;

public class ZaverecnaPracaFactory implements ResponseFactory<ZaverecnaPraca, ZaverecnaPracaDto>{
    
    @Override
    public ZaverecnaPracaDto transformToDto(ZaverecnaPraca entity) {
        ZaverecnaPracaDto dto = new ZaverecnaPracaDto();
        
        dto.setId(entity.getId());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDepartment(entity.getDepartment());
        dto.setType(entity.getType());
        dto.setStatus(entity.getStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dto.setPublishedOn(entity.getPublishedOn().format(formatter));
        dto.setDeadline(entity.getDeadline().format(formatter));
        dto.setSupervisor(new ShorterPedagogFactory().transformToDto(entity.getSupervisor()));
        if(entity.getAuthor() != null){
            dto.setAuthor(new ShorterStudentFactory().transformToDto(entity.getAuthor()));
        }
        else{
            dto.setAuthor(null);
        }
        return dto;
    }
    
}
