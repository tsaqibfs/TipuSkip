package com.tipuskip.dao.impl;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.dao.PilihanResponsDAO;
import com.tipuskip.model.PilihanRespons;
import com.tipuskip.model.TingkatRisiko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementasi JDBC untuk PilihanResponsDAO.
 * Bagian Tanggung Jawab Anggota 2.
 */
public class PilihanResponsDAOImpl implements PilihanResponsDAO {
    private static final Logger logger = LoggerFactory.getLogger(PilihanResponsDAOImpl.class);

    @Override
    public boolean save(PilihanRespons resp) {
        String sql = "INSERT INTO pilihan_respons (langkah_id, teks_respons, tingkat_risiko, poin_skor, umpan_balik, konsekuensi, langkah_berikutnya) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, resp.getLangkahId());
            ps.setString(2, resp.getTeksRespons());
            ps.setString(3, resp.getTingkatRisiko().name());
            ps.setInt(4, resp.getPoinSkor());
            ps.setString(5, resp.getUmpanBalik());
            ps.setString(6, resp.getKonsekuensi());
            if (resp.getLangkahBerikutnya() != null) {
                ps.setInt(7, resp.getLangkahBerikutnya());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        resp.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Gagal menyimpan pilihan respons: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<PilihanRespons> findById(Integer id) {
        String sql = "SELECT * FROM pilihan_respons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari pilihan respons id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<PilihanRespons> findAll() {
        List<PilihanRespons> list = new ArrayList<>();
        String sql = "SELECT * FROM pilihan_respons ORDER BY langkah_id ASC, id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil semua pilihan respons: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<PilihanRespons> findByLangkahId(int langkahId) {
        List<PilihanRespons> list = new ArrayList<>();
        String sql = "SELECT * FROM pilihan_respons WHERE langkah_id = ? ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langkahId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Gagal mencari pilihan respons untuk langkah id {}: {}", langkahId, e.getMessage());
        }
        return list;
    }

    @Override
    public boolean update(PilihanRespons resp) {
        String sql = "UPDATE pilihan_respons SET langkah_id = ?, teks_respons = ?, tingkat_risiko = ?, poin_skor = ?, umpan_balik = ?, konsekuensi = ?, langkah_berikutnya = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resp.getLangkahId());
            ps.setString(2, resp.getTeksRespons());
            ps.setString(3, resp.getTingkatRisiko().name());
            ps.setInt(4, resp.getPoinSkor());
            ps.setString(5, resp.getUmpanBalik());
            ps.setString(6, resp.getKonsekuensi());
            if (resp.getLangkahBerikutnya() != null) {
                ps.setInt(7, resp.getLangkahBerikutnya());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, resp.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal memperbarui pilihan respons id {}: {}", resp.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM pilihan_respons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus pilihan respons id {}: {}", id, e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteByLangkahId(int langkahId) {
        String sql = "DELETE FROM pilihan_respons WHERE langkah_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langkahId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Gagal menghapus pilihan respons dari langkah id {}: {}", langkahId, e.getMessage());
        }
        return false;
    }

    private PilihanRespons mapResultSet(ResultSet rs) throws SQLException {
        PilihanRespons resp = new PilihanRespons();
        resp.setId(rs.getInt("id"));
        resp.setLangkahId(rs.getInt("langkah_id"));
        resp.setTeksRespons(rs.getString("teks_respons"));
        resp.setTingkatRisiko(TingkatRisiko.valueOf(rs.getString("tingkat_risiko")));
        resp.setPoinSkor(rs.getInt("poin_skor"));
        resp.setUmpanBalik(rs.getString("umpan_balik"));
        resp.setKonsekuensi(rs.getString("konsekuensi"));
        int next = rs.getInt("langkah_berikutnya");
        if (!rs.wasNull()) {
            resp.setLangkahBerikutnya(next);
        } else {
            resp.setLangkahBerikutnya(null);
        }
        return resp;
    }
}
