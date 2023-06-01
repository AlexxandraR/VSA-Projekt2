package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.util.ArrayList;
import java.util.List;
import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.entities.Pedagog;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogDto;
import sk.stuba.fei.uim.vsa.pr2.response.PedagogPageDto;

public class PedagogPageFactory implements ResponseFactory<Page1, PedagogPageDto>{

    @Override
    public PedagogPageDto transformToDto(Page1 entity) {
        PedagogPageDto ppdto = new PedagogPageDto();
        
        List<PedagogDto> lp = new ArrayList();
        
        for(Object p : entity.getContent()){
            lp.add(new PedagogFactory().transformToDto((Pedagog) p));
        }
        
        ppdto.setContent(lp);
        ppdto.setPage(new PageFactory().transformToDto(entity));
        
        return ppdto;
    }
    
}
