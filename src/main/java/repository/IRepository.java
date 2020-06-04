package repository;

import java.util.List;

public interface IRepository<T> {
    void add(T t);
    void update(T t);
    T findById(int id);
    List<T> findAll();
}
