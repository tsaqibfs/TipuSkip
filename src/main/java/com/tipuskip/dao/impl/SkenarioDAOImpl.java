package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.SkenarioDAO;
import com.tipuskip.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk SkenarioDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class SkenarioDAOImpl implements SkenarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(SkenarioDAOImpl.class);

    @Override
    public boolean save(Skenario skenario) {
        String sql = "INSERT INTO skenario (judul, deskripsi, jenis, kategori, tingkat_kesulitan, status_aktif) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, skenario.getJudul());
            ps.setString(2, skenario.getDeskripsi());
            ps.setString(3, skenario.getJenis().name());
            ps.setString(4, skenario.getKategori());
            ps.setString(5, skenario.getTingkatKesulitan().name());
            ps.setBoolean(6, skenario.isStatusAktif());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        skenario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan skenario: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Skenario> findById(Integer id) {
        String sql = "SELECT * FROM skenario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSkenario(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari skenario id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Skenario> findWithFullStepsById(int id) {
        Optional<Skenario> skenarioOpt = findById(id);
        if (skenarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Skenario skenario = skenarioOpt.get();
        String sqlLangkah = "SELECT * FROM langkah_skenario WHERE skenario_id = ? ORDER BY nomor_langkah ASC";
        String sqlRespons = "SELECT * FROM pilihan_respons WHERE langkah_id = ? ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement psLangkah = conn.prepareStatement(sqlLangkah)) {
            psLangkah.setInt(1, id);
            try (ResultSet rsLangkah = psLangkah.executeQuery()) {
                while (rsLangkah.next()) {
                    LangkahSkenario langkah = new LangkahSkenario();
                    langkah.setId(rsLangkah.getInt("id"));
                    langkah.setSkenarioId(rsLangkah.getInt("skenario_id"));
                    langkah.setNomorLangkah(rsLangkah.getInt("nomor_langkah"));
                    langkah.setNamaPengirim(rsLangkah.getString("nama_pengirim"));
                    langkah.setPesanPengirim(rsLangkah.getString("pesan_pengirim"));
                    langkah.setTipeKonten(rsLangkah.getString("tipe_konten"));

                    // Ambil respons untuk tiap langkah
                    try (PreparedStatement psResp = conn.prepareStatement(sqlRespons)) {
                        psResp.setInt(1, langkah.getId());
                        try (ResultSet rsResp = psResp.executeQuery()) {
                            while (rsResp.next()) {
                                PilihanRespons resp = new PilihanRespons();
                                resp.setId(rsResp.getInt("id"));
                                resp.setLangkahId(rsResp.getInt("langkah_id"));
                                resp.setTeksRespons(rsResp.getString("teks_respons"));
                                resp.setTingkatRisiko(TingkatRisiko.valueOf(rsResp.getString("tingkat_risiko")));
                                resp.setPoinSkor(rsResp.getInt("poin_skor"));
                                resp.setUmpanBalik(rsResp.getString("umpan_balik"));
                                resp.setKonsekuensi(rsResp.getString("konsekuensi"));
                                int next = rsResp.getInt("langkah_berikutnya");
                                if (!rsResp.wasNull()) {
                                    resp.setLangkahBerikutnya(next);
                                }
                                langkah.tambahPilihanRespons(resp);
                            }
                        }
                    }
                    skenario.tambahLangkah(langkah);
                }
            }
            return Optional.of(skenario);
        } catch (SQLException e) {
            logger.error("Gagal mengambil detail langkah skenario id {}: {}", id, e.getMessage());
        }
        return Optional.of(skenario);
    }

    @Override
    public List<Skenario> findAll() {
        List<Skenario> list = new ArrayList<>();
        String sql = "SELECT * FROM skenario ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToSkenario(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua skenario: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<Skenario> findAllActive() {
        List<Skenario> list = new ArrayList<>();
        String sql = "SELECT * FROM skenario WHERE status_aktif = TRUE ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToSkenario(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil skenario aktif: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<Skenario> findByJenis(JenisSkenario jenis) {
        List<Skenario> list = new ArrayList<>();
        String sql = "SELECT * FROM skenario WHERE jenis = ? AND status_aktif = TRUE ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jenis.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSkenario(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari skenario berdasarkan jenis {}: {}", jenis, e.getMessage());
        }
        return list;
    }

    @Override
    public List<Skenario> findByKategori(String kategori) {
        List<Skenario> list = new ArrayList<>();
        String sql = "SELECT * FROM skenario WHERE kategori LIKE ? AND status_aktif = TRUE";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + kategori + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSkenario(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari skenario berdasarkan kategori {}: {}", kategori, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(Skenario skenario) {
        String sql = "UPDATE skenario SET judul = ?, deskripsi = ?, jenis = ?, kategori = ?, tingkat_kesulitan = ?, status_aktif = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, skenario.getJudul());
            ps.setString(2, skenario.getDeskripsi());
            ps.setString(3, skenario.getJenis().name());
            ps.setString(4, skenario.getKategori());
            ps.setString(5, skenario.getTingkatKesulitan().name());
            ps.setBoolean(6, skenario.isStatusAktif());
            ps.setInt(7, skenario.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal memperbarui skenario id {}: {}", skenario.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM skenario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus skenario id {}: {}", id, e.getMessage());
        }
        return false;
    }

    private Skenario mapResultSetToSkenario(ResultSet rs) throws SQLException {
        Skenario s = new Skenario();
        s.setId(rs.getInt("id"));
        s.setJudul(rs.getString("judul"));
        s.setDeskripsi(rs.getString("deskripsi"));
        s.setJenis(JenisSkenario.valueOf(rs.getString("jenis")));
        s.setKategori(rs.getString("kategori"));
        s.setTingkatKesulitan(TingkatKesulitan.valueOf(rs.getString("tingkat_kesulitan")));
        s.setStatusAktif(rs.getBoolean("status_aktif"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            s.setCreatedAt(ts.toLocalDateTime());
        }
        return s;
    }
}
