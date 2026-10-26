package com.tipuskip.dao;

import com.tipuskip.model.Pengguna;

import java.util.Optional;

public interface PenggunaDAO extends GenericDAO<Pengguna, Integer> {
    Optional<Pengguna> findByUsername(String username);
    Optional<Pengguna> findByEmail(String email);
    Optional<Pengguna> authenticate(String username, String rawPassword);
}
