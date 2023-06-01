package sk.stuba.fei.uim.vsa.pr2.response.factory;

public interface ResponseFactory<R, T> {
    T transformToDto(R entity);
}
