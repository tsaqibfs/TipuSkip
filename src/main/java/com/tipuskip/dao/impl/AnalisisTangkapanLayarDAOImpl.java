package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.AnalisisTangkapanLayarDAO;
import com.tipuskip.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk AnalisisTangkapanLayarDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class AnalisisTangkapanLayarDAOImpl implements AnalisisTangkapanLayarDAO {
    private static final Logger logger = LoggerFactory.getLogger(AnalisisTangkapanLayarDAOImpl.class);

    @Override
    public boolean save(AnalisisTangkapanLayar atl) {
        String sql = "INSERT INTO analisis_tangkapan_layar (pengguna_id, nama_file, teks_ekstraksi, tingkat_risiko, total_skor_risiko, alasan_penilaian, saran_verifikasi, disclaimer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO detail_analisis (analisis_id, indikator_id, teks_ditemukan, bobot_terhitung) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, atl.getPenggunaId());
                ps.setString(2, atl.getNamaFile());
                ps.setString(3, atl.getTeksEkstraksi());
                ps.setString(4, atl.getTingkatRisiko().name());
                ps.setInt(5, atl.getTotalSkorRisiko());
                ps.setString(6, atl.getAlasanPenilaian());
                ps.setString(7, atl.getSaranVerifikasi());
                ps.setString(8, atl.getDisclaimer());

                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            atl.setId(rs.getInt(1));
                        }
                    }

                    // Simpan detail indikator yang cocok
                    if (atl.getDaftarDetail() != null && !atl.getDaftarDetail().isEmpty()) {
                        try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                            for (DetailAnalisis da : atl.getDaftarDetail()) {
                                psDetail.setInt(1, atl.getId());
                                psDetail.setInt(2, da.getIndikatorId());
                                psDetail.setString(3, da.getTeksDitemukan());
                                psDetail.setInt(4, da.getBobotTerhitung());
                                psDetail.addBatch();
                            }
                            psDetail.executeBatch();
                        }
                    }
                    conn.commit();
                    return true;
                }
            } catch (SQLException ex) {
                conn.rollback();
                logger.error("Rollback karena error saat menyimpan analisis tangkapan layar: {}", ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Database connection error saat save analisis: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<AnalisisTangkapanLayar> findById(Integer id) {
        String sql = "SELECT * FROM analisis_tangkapan_layar WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari riwayat analisis id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<AnalisisTangkapanLayar> findWithDetailsById(int id) {
        Optional<AnalisisTangkapanLayar> opt = findById(id);
        if (opt.isEmpty()) return Optional.empty();

        AnalisisTangkapanLayar atl = opt.get();
        String sqlDetail = "SELECT d.*, i.nama_indikator, i.pola_pencocokan, i.tipe_pencocokan, i.deskripsi, i.saran_verifikasi as saran_ind, i.status_aktif " +
                "FROM detail_analisis d " +
                "JOIN indikator_penipuan i ON d.indikator_id = i.id " +
                "WHERE d.analisis_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetailAnalisis da = new DetailAnalisis();
                    da.setId(rs.getInt("id"));
                    da.setAnalisisId(rs.getInt("analisis_id"));
                    da.setIndikatorId(rs.getInt("indikator_id"));
                    da.setTeksDitemukan(rs.getString("teks_ditemukan"));
                    da.setBobotTerhitung(rs.getInt("bobot_terhitung"));

                    IndikatorPenipuan ip = new IndikatorPenipuan();
                    ip.setId(rs.getInt("indikator_id"));
                    ip.setNamaIndikator(rs.getString("nama_indikator"));
                    ip.setPolaPencocokan(rs.getString("pola_pencocokan"));
                    ip.setTipePencocokan(TipePencocokan.valueOf(rs.getString("tipe_pencocokan")));
                    ip.setBobotRisiko(rs.getInt("bobot_terhitung"));
                    ip.setDeskripsi(rs.getString("deskripsi"));
                    ip.setSaranVerifikasi(rs.getString("saran_ind"));
                    ip.setStatusAktif(rs.getBoolean("status_aktif"));

                    da.setIndikator(ip);
                    atl.tambahDetail(da);
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil detail analisis id {}: {}", id, e.getMessage());
        }
        return Optional.of(atl);
    }

    @Override
    public List<AnalisisTangkapanLayar> findAll() {
        List<AnalisisTangkapanLayar> list = new ArrayList<>();
        String sql = "SELECT * FROM analisis_tangkapan_layar ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua riwayat analisis: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<AnalisisTangkapanLayar> findByPenggunaId(int penggunaId) {
        List<AnalisisTangkapanLayar> list = new ArrayList<>();
        String sql = "SELECT * FROM analisis_tangkapan_layar WHERE pengguna_id = ? ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, penggunaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil riwayat analisis pengguna {}: {}", penggunaId, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(AnalisisTangkapanLayar entity) {
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM analisis_tangkapan_layar WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus riwayat analisis id {}: {}", id, e.getMessage());
        }
        return false;
    }

    private AnalisisTangkapanLayar mapResultSet(ResultSet rs) throws SQLException {
        AnalisisTangkapanLayar atl = new AnalisisTangkapanLayar();
        atl.setId(rs.getInt("id"));
        atl.setPenggunaId(rs.getInt("pengguna_id"));
        atl.setNamaFile(rs.getString("nama_file"));
        atl.setTeksEkstraksi(rs.getString("teks_ekstraksi"));
        atl.setTingkatRisiko(TingkatRisikoAnalisis.valueOf(rs.getString("tingkat_risiko")));
        atl.setTotalSkorRisiko(rs.getInt("total_skor_risiko"));
        atl.setAlasanPenilaian(rs.getString("alasan_penilaian"));
        atl.setSaranVerifikasi(rs.getString("saran_verifikasi"));
        atl.setDisclaimer(rs.getString("disclaimer"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            atl.setCreatedAt(ts.toLocalDateTime());
        }
        return atl;
    }
}
