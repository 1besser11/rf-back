package com.nikita.development.rf.service;

import java.util.List;

public interface IService<T> {

    List<T> findAll(int size, int page);

    T save(T entity);

    T find(long id);

    T update(long id, T entity);

    void delete(long id);
}
