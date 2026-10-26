package com.tipuskip.model;

/**
 * Role akses pengguna dalam aplikasi TipuSkip.
 */
public enum Role {
    PENGGUNA,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) return PENGGUNA;
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENGGUNA;
        }
    }
}
