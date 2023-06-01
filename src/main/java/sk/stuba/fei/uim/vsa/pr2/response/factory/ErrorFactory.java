package sk.stuba.fei.uim.vsa.pr2.response.factory;

import sk.stuba.fei.uim.vsa.pr2.response.ErrorDto;
import sk.stuba.fei.uim.vsa.pr2.Error;

public class ErrorFactory implements ResponseFactory<Error, ErrorDto>{
    

    @Override
    public ErrorDto transformToDto(Error entity) {
        ErrorDto edto = new ErrorDto();
        edto.setType(entity.getType());
        edto.setTrace(entity.getTrace());
        return edto;
    }
}
