package sk.stuba.fei.uim.vsa.pr2.response.factory;

import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.pr2.response.StudentDto;

public class StudentFactory implements ResponseFactory<Student, StudentDto>{
    
    @Override
    public StudentDto transformToDto(Student entity) {
        StudentDto dto = new StudentDto();
        
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
            dto.setThesis(new ZaverecnaPracaFactory().transformToDto(entity.getThesis()));
        }
        
        return dto;
    }
}
