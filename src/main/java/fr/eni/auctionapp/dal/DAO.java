package fr.eni.auctionapp.dal;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> findAll();

    Optional<T> findById(int id);

    int insert(T entity);

    void remove(T entity);
}
