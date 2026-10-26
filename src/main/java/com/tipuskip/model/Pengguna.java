package com.tipuskip.model;

import java.time.LocalDateTime;

/**
 * Entitas data Pengguna aplikasi TipuSkip.
 * Menerapkan prinsip OOP Enkapsulasi dengan atribut private dan getter/setter.
 */
public class Pengguna {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;
    private LocalDateTime createdAt;

    public Pengguna() {
        this.role = Role.PENGGUNA;
        this.createdAt = LocalDateTime.now();
    }

    public Pengguna(int id, String username, String email, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.PENGGUNA;
        this.createdAt = LocalDateTime.now();
    }

    public Pengguna(String username, String email, String passwordHash, Role role) {
        this(0, username, email, passwordHash, role);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "Pengguna{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
