package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.HasilSimulasiDAO;
import com.tipuskip.model.HasilSimulasi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk HasilSimulasiDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class HasilSimulasiDAOImpl implements HasilSimulasiDAO {
    private static final Logger logger = LoggerFactory.getLogger(HasilSimulasiDAOImpl.class);

    @Override
    public boolean save(HasilSimulasi hs) {
        String sql = "INSERT INTO hasil_simulasi (pengguna_id, skenario_id, total_skor, status_evaluasi, durasi_detik, waktu_mulai, waktu_selesai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hs.getPenggunaId());
            ps.setInt(2, hs.getSkenarioId());
            ps.setInt(3, hs.getTotalSkor());
            ps.setString(4, hs.getStatusEvaluasi());
            ps.setInt(5, hs.getDurasiDetik());
            ps.setTimestamp(6, hs.getWaktuMulai() != null ? Timestamp.valueOf(hs.getWaktuMulai()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setTimestamp(7, hs.getWaktuSelesai() != null ? Timestamp.valueOf(hs.getWaktuSelesai()) : Timestamp.valueOf(java.time.LocalDateTime.now()));

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        hs.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan hasil simulasi: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<HasilSimulasi> findById(Integer id) {
        String sql = "SELECT h.*, s.judul as judul_skenario FROM hasil_simulasi h JOIN skenario s ON h.skenario_id = s.id WHERE h.id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari hasil simulasi id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<HasilSimulasi> findAll() {
        List<HasilSimulasi> list = new ArrayList<>();
        String sql = "SELECT h.*, s.judul as judul_skenario FROM hasil_simulasi h JOIN skenario s ON h.skenario_id = s.id ORDER BY h.id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua hasil simulasi: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<HasilSimulasi> findByPenggunaId(int penggunaId) {
        List<HasilSimulasi> list = new ArrayList<>();
        String sql = "SELECT h.*, s.judul as judul_skenario FROM hasil_simulasi h JOIN skenario s ON h.skenario_id = s.id WHERE h.pengguna_id = ? ORDER BY h.id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, penggunaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari riwayat simulasi pengguna {}: {}", penggunaId, e.getMessage());
        }
        return list;
    }

    @Override
    public List<HasilSimulasi> findBySkenarioId(int skenarioId) {
        List<HasilSimulasi> list = new ArrayList<>();
        String sql = "SELECT h.*, s.judul as judul_skenario FROM hasil_simulasi h JOIN skenario s ON h.skenario_id = s.id WHERE h.skenario_id = ? ORDER BY h.id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skenarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari hasil berdasarkan skenario id {}: {}", skenarioId, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(HasilSimulasi entity) {
        // Riwayat simulasi bersifat append-only, update jarang dilakukan
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM hasil_simulasi WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus riwayat simulasi id {}: {}", id, e.getMessage());
        }
        return false;
    }

    private HasilSimulasi mapResultSet(ResultSet rs) throws SQLException {
        HasilSimulasi hs = new HasilSimulasi();
        hs.setId(rs.getInt("id"));
        hs.setPenggunaId(rs.getInt("pengguna_id"));
        hs.setSkenarioId(rs.getInt("skenario_id"));
        try {
            hs.setJudulSkenario(rs.getString("judul_skenario"));
        } catch (SQLException ignored) {
            hs.setJudulSkenario("Skenario #" + hs.getSkenarioId());
        }
        hs.setTotalSkor(rs.getInt("total_skor"));
        hs.setStatusEvaluasi(rs.getString("status_evaluasi"));
        hs.setDurasiDetik(rs.getInt("durasi_detik"));
        Timestamp wm = rs.getTimestamp("waktu_mulai");
        if (wm != null) hs.setWaktuMulai(wm.toLocalDateTime());
        Timestamp ws = rs.getTimestamp("waktu_selesai");
        if (ws != null) hs.setWaktuSelesai(ws.toLocalDateTime());
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) hs.setCreatedAt(ca.toLocalDateTime());
        return hs;
    }
}
