package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.DetailAnalisisDAO;
import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.model.TipePencocokan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk DetailAnalisisDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class DetailAnalisisDAOImpl implements DetailAnalisisDAO {
    private static final Logger logger = LoggerFactory.getLogger(DetailAnalisisDAOImpl.class);

    @Override
    public boolean save(DetailAnalisis da) {
        String sql = "INSERT INTO detail_analisis (analisis_id, indikator_id, teks_ditemukan, bobot_terhitung) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, da.getAnalisisId());
            ps.setInt(2, da.getIndikatorId());
            ps.setString(3, da.getTeksDitemukan());
            ps.setInt(4, da.getBobotTerhitung());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        da.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan detail analisis: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<DetailAnalisis> findById(Integer id) {
        String sql = "SELECT da.*, ip.nama_indikator, ip.pola_pencocokan, ip.tipe_pencocokan, ip.bobot_risiko, ip.deskripsi, ip.saran_verifikasi, ip.status_aktif " +
                     "FROM detail_analisis da " +
                     "LEFT JOIN indikator_penipuan ip ON da.indikator_id = ip.id " +
                     "WHERE da.id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari detail analisis id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<DetailAnalisis> findAll() {
        List<DetailAnalisis> list = new ArrayList<>();
        String sql = "SELECT da.*, ip.nama_indikator, ip.pola_pencocokan, ip.tipe_pencocokan, ip.bobot_risiko, ip.deskripsi, ip.saran_verifikasi, ip.status_aktif " +
                     "FROM detail_analisis da " +
                     "LEFT JOIN indikator_penipuan ip ON da.indikator_id = ip.id " +
                     "ORDER BY da.analisis_id ASC, da.id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua detail analisis: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<DetailAnalisis> findByAnalisisId(int analisisId) {
        List<DetailAnalisis> list = new ArrayList<>();
        String sql = "SELECT da.*, ip.nama_indikator, ip.pola_pencocokan, ip.tipe_pencocokan, ip.bobot_risiko, ip.deskripsi, ip.saran_verifikasi, ip.status_aktif " +
                     "FROM detail_analisis da " +
                     "LEFT JOIN indikator_penipuan ip ON da.indikator_id = ip.id " +
                     "WHERE da.analisis_id = ? ORDER BY da.id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, analisisId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari detail analisis untuk analisis id {}: {}", analisisId, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(DetailAnalisis da) {
        String sql = "UPDATE detail_analisis SET analisis_id = ?, indikator_id = ?, teks_ditemukan = ?, bobot_terhitung = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, da.getAnalisisId());
            ps.setInt(2, da.getIndikatorId());
            ps.setString(3, da.getTeksDitemukan());
            ps.setInt(4, da.getBobotTerhitung());
            ps.setInt(5, da.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal memperbarui detail analisis id {}: {}", da.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM detail_analisis WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus detail analisis id {}: {}", id, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteByAnalisisId(int analisisId) {
        String sql = "DELETE FROM detail_analisis WHERE analisis_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, analisisId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus detail analisis untuk analisis id {}: {}", analisisId, e.getMessage());
        }
        return false;
    }

    private DetailAnalisis mapResultSet(ResultSet rs) throws SQLException {
        DetailAnalisis da = new DetailAnalisis();
        da.setId(rs.getInt("id"));
        da.setAnalisisId(rs.getInt("analisis_id"));
        da.setIndikatorId(rs.getInt("indikator_id"));
        da.setTeksDitemukan(rs.getString("teks_ditemukan"));
        da.setBobotTerhitung(rs.getInt("bobot_terhitung"));

        // Map indikator jika tersedia
        String namaIndikator = rs.getString("nama_indikator");
        if (namaIndikator != null) {
            IndikatorPenipuan ip = new IndikatorPenipuan();
            ip.setId(rs.getInt("indikator_id"));
            ip.setNamaIndikator(namaIndikator);
            ip.setPolaPencocokan(rs.getString("pola_pencocokan"));
            ip.setTipePencocokan(TipePencocokan.valueOf(rs.getString("tipe_pencocokan")));
            ip.setBobotRisiko(rs.getInt("bobot_risiko"));
            ip.setDeskripsi(rs.getString("deskripsi"));
            ip.setSaranVerifikasi(rs.getString("saran_verifikasi"));
            ip.setStatusAktif(rs.getBoolean("status_aktif"));
            da.setIndikator(ip);
        }

        return da;
    }
}
