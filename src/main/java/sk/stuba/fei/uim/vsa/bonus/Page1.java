package sk.stuba.fei.uim.vsa.bonus;

import java.util.List;

public class Page1<T> implements Page<T>{
    private List<T> content;
    private Pageable pageable;
    private Long totalElements;

    public Page1() {}

    public Page1(List<T> content, Pageable pageable, Long totalElements) {
        this.content = content;
        this.pageable = pageable;
        this.totalElements = totalElements;
    }

    @Override
    public List<T> getContent() {
        return this.content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Long getTotalElements() {
        return this.totalElements;
    }

    @Override
    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageable.getPageSize());
    }
}