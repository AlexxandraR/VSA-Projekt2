package sk.stuba.fei.uim.vsa.bonus;

public class Pageable1 implements Pageable{

    private int page;
    private int size;

    public Pageable1() {}

    public Pageable1(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public Pageable of(int page, int size) {
        return new Pageable1(page, size);
    }

    @Override
    public Pageable first() {
        return new Pageable1(0, size);
    }

    @Override
    public Pageable previous() {
        return page == 0 ? this : new Pageable1(page - 1, size);
    }

    @Override
    public Pageable next() {
        return new Pageable1(page + 1, size);
    }

    @Override
    public Integer getPageNumber() {
        return page;
    }

    @Override
    public Integer getPageSize() {
        return size;
    }

}