package sk.stuba.fei.uim.vsa.pr2.response.factory;

import sk.stuba.fei.uim.vsa.pr2.Message;
import sk.stuba.fei.uim.vsa.pr2.response.MessageDto;

public class MessageFactory implements ResponseFactory<Message, MessageDto>{

    @Override
    public MessageDto transformToDto(Message entity) {
        MessageDto mdto = new MessageDto();
        mdto.setCode(entity.getCode());
        mdto.setMessage(entity.getMessage());
        mdto.setError(new ErrorFactory().transformToDto(entity.getError()));
        return mdto;
    }
    
}
