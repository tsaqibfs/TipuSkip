package com.tipuskip.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic DAO Interface (Penerapan Konsep Generic & Interface Contract).
 * Memudahkan standardisasi operasi CRUD bagi seluruh entitas.
 */
public interface GenericDAO<T, ID> {
    boolean save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean update(T entity);
    boolean deleteById(ID id);
}
