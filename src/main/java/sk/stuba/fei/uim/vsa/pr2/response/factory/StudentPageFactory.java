package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.util.ArrayList;
import java.util.List;
import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.entities.Student;
import sk.stuba.fei.uim.vsa.pr2.response.StudentDto;
import sk.stuba.fei.uim.vsa.pr2.response.StudentPageDto;

public class StudentPageFactory implements ResponseFactory<Page1, StudentPageDto>{

    @Override
    public StudentPageDto transformToDto(Page1 entity) {
        StudentPageDto spdto = new StudentPageDto();
        
        List<StudentDto> ls = new ArrayList();
        
        for(Object s : entity.getContent()){
            ls.add(new StudentFactory().transformToDto((Student) s));
        }
        
        spdto.setContent(ls);
        spdto.setPage(new PageFactory().transformToDto(entity));
        
        return spdto;
    }
    
}
