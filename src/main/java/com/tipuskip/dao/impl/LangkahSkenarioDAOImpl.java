package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.LangkahSkenarioDAO;
import com.tipuskip.model.LangkahSkenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk LangkahSkenarioDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class LangkahSkenarioDAOImpl implements LangkahSkenarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(LangkahSkenarioDAOImpl.class);

    @Override
    public boolean save(LangkahSkenario langkah) {
        String sql = "INSERT INTO langkah_skenario (skenario_id, nomor_langkah, nama_pengirim, pesan_pengirim, tipe_konten) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, langkah.getSkenarioId());
            ps.setInt(2, langkah.getNomorLangkah());
            ps.setString(3, langkah.getNamaPengirim());
            ps.setString(4, langkah.getPesanPengirim());
            ps.setString(5, langkah.getTipeKonten());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        langkah.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan langkah skenario: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<LangkahSkenario> findById(Integer id) {
        String sql = "SELECT * FROM langkah_skenario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari langkah skenario id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<LangkahSkenario> findAll() {
        List<LangkahSkenario> list = new ArrayList<>();
        String sql = "SELECT * FROM langkah_skenario ORDER BY skenario_id ASC, nomor_langkah ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua langkah skenario: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<LangkahSkenario> findBySkenarioId(int skenarioId) {
        List<LangkahSkenario> list = new ArrayList<>();
        String sql = "SELECT * FROM langkah_skenario WHERE skenario_id = ? ORDER BY nomor_langkah ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skenarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari langkah skenario untuk skenario id {}: {}", skenarioId, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(LangkahSkenario langkah) {
        String sql = "UPDATE langkah_skenario SET skenario_id = ?, nomor_langkah = ?, nama_pengirim = ?, pesan_pengirim = ?, tipe_konten = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langkah.getSkenarioId());
            ps.setInt(2, langkah.getNomorLangkah());
            ps.setString(3, langkah.getNamaPengirim());
            ps.setString(4, langkah.getPesanPengirim());
            ps.setString(5, langkah.getTipeKonten());
            ps.setInt(6, langkah.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal memperbarui langkah skenario id {}: {}", langkah.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM langkah_skenario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus langkah skenario id {}: {}", id, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteBySkenarioId(int skenarioId) {
        String sql = "DELETE FROM langkah_skenario WHERE skenario_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, skenarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus langkah skenario dari skenario id {}: {}", skenarioId, e.getMessage());
        }
        return false;
    }

    private LangkahSkenario mapResultSet(ResultSet rs) throws SQLException {
        LangkahSkenario langkah = new LangkahSkenario();
        langkah.setId(rs.getInt("id"));
        langkah.setSkenarioId(rs.getInt("skenario_id"));
        langkah.setNomorLangkah(rs.getInt("nomor_langkah"));
        langkah.setNamaPengirim(rs.getString("nama_pengirim"));
        langkah.setPesanPengirim(rs.getString("pesan_pengirim"));
        langkah.setTipeKonten(rs.getString("tipe_konten"));
        return langkah;
    }
}
