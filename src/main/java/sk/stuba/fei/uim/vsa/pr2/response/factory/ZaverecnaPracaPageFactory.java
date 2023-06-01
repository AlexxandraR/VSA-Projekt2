package sk.stuba.fei.uim.vsa.pr2.response.factory;

import java.util.ArrayList;
import java.util.List;
import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.entities.ZaverecnaPraca;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaDto;
import sk.stuba.fei.uim.vsa.pr2.response.ZaverecnaPracaPageDto;

public class ZaverecnaPracaPageFactory implements ResponseFactory<Page1, ZaverecnaPracaPageDto>{

    @Override
    public ZaverecnaPracaPageDto transformToDto(Page1 entity) {
        ZaverecnaPracaPageDto zvpdto = new ZaverecnaPracaPageDto();
        
        List<ZaverecnaPracaDto> lzv = new ArrayList();
        
        for(Object zv : entity.getContent()){
            lzv.add(new ZaverecnaPracaFactory().transformToDto((ZaverecnaPraca) zv));
        }
        
        zvpdto.setContent(lzv);
        zvpdto.setPage(new PageFactory().transformToDto(entity));
        
        return zvpdto;
    }
    
}
