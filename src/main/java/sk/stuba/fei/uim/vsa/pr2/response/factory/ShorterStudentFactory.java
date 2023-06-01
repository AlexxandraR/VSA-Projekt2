package sk.stuba.fei.uim.vsa.pr2.response.factory;

import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.pr2.response.ShorterStudentDto;

public class ShorterStudentFactory implements ResponseFactory<Student, ShorterStudentDto>{
    @Override
    public ShorterStudentDto transformToDto(Student entity){
        ShorterStudentDto dto = new ShorterStudentDto();
        
        dto.setId(entity.getAisId());
        dto.setAisId(entity.getAisId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setProgramme(entity.getProgramme());
        dto.setTerm(entity.getTerm());
        dto.setYear(entity.getYear());
        if(entity.getThesis() == null){
            dto.setThesis(null);
        }else{
            
            dto.setThesis(entity.getThesis().getId());
        }
        
        return dto;
    }
}
