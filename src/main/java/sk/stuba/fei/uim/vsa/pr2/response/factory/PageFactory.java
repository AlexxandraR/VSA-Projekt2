package sk.stuba.fei.uim.vsa.pr2.response.factory;

import sk.stuba.fei.uim.vsa.bonus.Page1;
import sk.stuba.fei.uim.vsa.pr2.response.PageDto;

public class PageFactory implements ResponseFactory<Page1, PageDto>{

    @Override
    public PageDto transformToDto(Page1 entity) {
        PageDto pdto = new PageDto();
        pdto.setSize(entity.getPageable().getPageSize());
        pdto.setTotalElements(entity.getTotalElements());
        pdto.setTotalPages(entity.getTotalPages());
        pdto.setNumber(entity.getPageable().getPageNumber());
        return pdto;
    }
    
}
