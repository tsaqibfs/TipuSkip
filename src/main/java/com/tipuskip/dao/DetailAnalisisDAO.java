package com.tipuskip.dao;

import com.tipuskip.model.DetailAnalisis;

import java.util.List;

/**
 * Interface DAO untuk entitas DetailAnalisis.
 * Bagian Tanggung Jawab Anggota 2.
 */
public interface DetailAnalisisDAO extends GenericDAO<DetailAnalisis, Integer> {
    List<DetailAnalisis> findByAnalisisId(int analisisId);
    boolean deleteByAnalisisId(int analisisId);
}
