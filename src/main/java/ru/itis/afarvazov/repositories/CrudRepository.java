package ru.itis.afarvazov.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    List<T> findAll();
    Optional<T> findById(ID id);

    void save(T entity);
    void update(T entity);
    void delete(T entity);

}
