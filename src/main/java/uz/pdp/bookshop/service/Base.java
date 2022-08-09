package uz.pdp.bookshop.service;

import java.util.List;

public interface Base<T> {

    boolean add(T t);
    List<T> get();
    boolean delete(Long id);
    void edit(T t);

}
