package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.IndikatorPenipuanDAO;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.TipePencocokan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk IndikatorPenipuanDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class IndikatorPenipuanDAOImpl implements IndikatorPenipuanDAO {
    private static final Logger logger = LoggerFactory.getLogger(IndikatorPenipuanDAOImpl.class);

    @Override
    public boolean save(IndikatorPenipuan indikator) {
        String sql = "INSERT INTO indikator_penipuan (nama_indikator, pola_pencocokan, tipe_pencocokan, bobot_risiko, deskripsi, saran_verifikasi, status_aktif) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, indikator.getNamaIndikator());
            ps.setString(2, indikator.getPolaPencocokan());
            ps.setString(3, indikator.getTipePencocokan().name());
            ps.setInt(4, indikator.getBobotRisiko());
            ps.setString(5, indikator.getDeskripsi());
            ps.setString(6, indikator.getSaranVerifikasi());
            ps.setBoolean(7, indikator.isStatusAktif());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        indikator.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan indikator penipuan: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<IndikatorPenipuan> findById(Integer id) {
        String sql = "SELECT * FROM indikator_penipuan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari indikator id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<IndikatorPenipuan> findAll() {
        List<IndikatorPenipuan> list = new ArrayList<>();
        String sql = "SELECT * FROM indikator_penipuan ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil daftar indikator: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<IndikatorPenipuan> findAllActive() {
        List<IndikatorPenipuan> list = new ArrayList<>();
        String sql = "SELECT * FROM indikator_penipuan WHERE status_aktif = TRUE ORDER BY bobot_risiko DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil indikator aktif: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public boolean toggleStatus(int id, boolean statusAktif) {
        String sql = "UPDATE indikator_penipuan SET status_aktif = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, statusAktif);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal toggle status indikator id {}: {}", id, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(IndikatorPenipuan indikator) {
        String sql = "UPDATE indikator_penipuan SET nama_indikator = ?, pola_pencocokan = ?, tipe_pencocokan = ?, bobot_risiko = ?, deskripsi = ?, saran_verifikasi = ?, status_aktif = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, indikator.getNamaIndikator());
            ps.setString(2, indikator.getPolaPencocokan());
            ps.setString(3, indikator.getTipePencocokan().name());
            ps.setInt(4, indikator.getBobotRisiko());
            ps.setString(5, indikator.getDeskripsi());
            ps.setString(6, indikator.getSaranVerifikasi());
            ps.setBoolean(7, indikator.isStatusAktif());
            ps.setInt(8, indikator.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal update indikator id {}: {}", indikator.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM indikator_penipuan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus indikator id {}: {}", id, e.getMessage());
        }
        return false;
    }

    private IndikatorPenipuan mapResultSet(ResultSet rs) throws SQLException {
        IndikatorPenipuan ind = new IndikatorPenipuan();
        ind.setId(rs.getInt("id"));
        ind.setNamaIndikator(rs.getString("nama_indikator"));
        ind.setPolaPencocokan(rs.getString("pola_pencocokan"));
        ind.setTipePencocokan(TipePencocokan.valueOf(rs.getString("tipe_pencocokan")));
        ind.setBobotRisiko(rs.getInt("bobot_risiko"));
        ind.setDeskripsi(rs.getString("deskripsi"));
        ind.setSaranVerifikasi(rs.getString("saran_verifikasi"));
        ind.setStatusAktif(rs.getBoolean("status_aktif"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            ind.setCreatedAt(ts.toLocalDateTime());
        }
        return ind;
    }
}
