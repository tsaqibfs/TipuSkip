package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.PenggunaDAO;
import com.tipuskip.model.Pengguna;
import com.tipuskip.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk PenggunaDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class PenggunaDAOImpl implements PenggunaDAO {
    private static final Logger logger = LoggerFactory.getLogger(PenggunaDAOImpl.class.getSimpleName());

    @Override
    public boolean save(Pengguna pengguna) {
        String sql = "INSERT INTO pengguna (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pengguna.getUsername());
            ps.setString(2, pengguna.getEmail());
            ps.setString(3, hashPassword(pengguna.getPasswordHash()));
            ps.setString(4, pengguna.getRole().name());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pengguna.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan pengguna {}: {}", pengguna.getUsername(), e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Pengguna> findById(Integer id) {
        String sql = "SELECT * FROM pengguna WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPengguna(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari pengguna berdasarkan id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Pengguna> findByUsername(String username) {
        String sql = "SELECT * FROM pengguna WHERE username = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPengguna(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari pengguna {}: {}", username, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Pengguna> findByEmail(String email) {
        String sql = "SELECT * FROM pengguna WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPengguna(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari pengguna berdasarkan email {}: {}", email, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Pengguna> authenticate(String username, String rawPassword) {
        Optional<Pengguna> userOpt = findByUsername(username);
        if (userOpt.isPresent()) {
            Pengguna user = userOpt.get();
            String hashedInput = hashPassword(rawPassword);
            if (hashedInput.equals(user.getPasswordHash()) || rawPassword.equals(user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Pengguna> findAll() {
        List<Pengguna> list = new ArrayList<>();
        String sql = "SELECT * FROM pengguna ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPengguna(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua pengguna: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET email = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pengguna.getEmail());
            ps.setString(2, pengguna.getRole().name());
            ps.setInt(3, pengguna.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal update pengguna: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM pengguna WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus pengguna id {}: {}", id, e.getMessage());
        }
        return false;
    }

    private Pengguna mapResultSetToPengguna(ResultSet rs) throws SQLException {
        Pengguna p = new Pengguna();
        p.setId(rs.getInt("id"));
        p.setUsername(rs.getString("username"));
        p.setEmail(rs.getString("email"));
        p.setPasswordHash(rs.getString("password_hash"));
        p.setRole(Role.fromString(rs.getString("role")));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            p.setCreatedAt(ts.toLocalDateTime());
        }
        return p;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }
}
