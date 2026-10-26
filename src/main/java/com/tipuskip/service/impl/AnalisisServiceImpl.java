package com.tipuskip.service.impl;

import com.tipuskip.analyzer.*;
import com.tipuskip.dao.AnalisisTangkapanLayarDAO;
import com.tipuskip.dao.IndikatorPenipuanDAO;
import com.tipuskip.dao.impl.AnalisisTangkapanLayarDAOImpl;
import com.tipuskip.dao.impl.IndikatorPenipuanDAOImpl;
import com.tipuskip.model.AnalisisTangkapanLayar;
import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.IndikatorPenipuan;
import com.tipuskip.service.IAnalisisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Implementasi IAnalisisService (Alur Analisis Tangkapan Layar Lengkap).
 * Bagian Tanggung Jawab Anggota 3.
 */
public class AnalisisServiceImpl implements IAnalisisService {
    private static final Logger logger = LoggerFactory.getLogger(AnalisisServiceImpl.class);

    private final ITextExtractor textExtractor;
    private final IndikatorMatcher indikatorMatcher;
    private final RiskEvaluator riskEvaluator;
    private final IndikatorPenipuanDAO indikatorDAO;
    private final AnalisisTangkapanLayarDAO analisisDAO;

    public AnalisisServiceImpl() {
        this(
                resolveTextExtractor(),
                new IndikatorMatcher(),
                new RiskEvaluator(),
                new IndikatorPenipuanDAOImpl(),
                new AnalisisTangkapanLayarDAOImpl()
        );
    }

    public AnalisisServiceImpl(ITextExtractor textExtractor,
                               IndikatorMatcher indikatorMatcher,
                               RiskEvaluator riskEvaluator,
                               IndikatorPenipuanDAO indikatorDAO,
                               AnalisisTangkapanLayarDAO analisisDAO) {
        this.textExtractor = textExtractor;
        this.indikatorMatcher = indikatorMatcher;
        this.riskEvaluator = riskEvaluator;
        this.indikatorDAO = indikatorDAO;
        this.analisisDAO = analisisDAO;
    }

    private static ITextExtractor resolveTextExtractor() {
        try {
            TesseractTextExtractor tesseract = new TesseractTextExtractor();
            if (tesseract.isAvailable()) {
                return tesseract;
            }
        } catch (Throwable t) {
            logger.warn("TesseractTextExtractor tidak dapat diinisialisasi, beralih ke MockTextExtractor: {}", t.getMessage());
        }
        return new MockTextExtractor();
    }

    @Override
    public AnalisisTangkapanLayar prosesAnalisis(File imageFile, int penggunaId) throws Exception {
        if (imageFile == null || !imageFile.exists()) {
            throw new IllegalArgumentException("Berkas gambar tidak ditemukan atau belum dipilih.");
        }

        logger.info("Memulai ekstraksi OCR untuk gambar: {}", imageFile.getName());

        // 1. Ekstraksi teks melalui OCR (Tess4J / Fallback)
        String teksEkstraksi = "";
        try {
            teksEkstraksi = textExtractor.extractText(imageFile);
        } catch (Exception e) {
            logger.warn("OCR mengalami error, mencoba fallback text extractor...", e);
            teksEkstraksi = new MockTextExtractor().extractText(imageFile);
        }

        // 2. Ambil daftar indikator penipuan aktif dari database (atau fallback memory)
        List<IndikatorPenipuan> indikatorList = indikatorDAO.findAllActive();
        if (indikatorList.isEmpty()) {
            indikatorList = buatIndikatorDefault();
        }

        // 3. Pencocokan pola & regex
        List<DetailAnalisis> detailList = indikatorMatcher.cocokkan(teksEkstraksi, indikatorList);

        // 4. Kalkulasi tingkat risiko dan penyusunan edukasi
        HasilPenilaianRisiko hasilPenilaian = riskEvaluator.evaluasi(detailList);

        // 5. Susun entitas AnalisisTangkapanLayar
        AnalisisTangkapanLayar hasil = new AnalisisTangkapanLayar();
        hasil.setPenggunaId(penggunaId);
        hasil.setNamaFile(imageFile.getName());
        hasil.setTeksEkstraksi(teksEkstraksi);
        hasil.setTingkatRisiko(hasilPenilaian.getTingkatRisiko());
        hasil.setTotalSkorRisiko(hasilPenilaian.getTotalSkorRisiko());
        hasil.setAlasanPenilaian(hasilPenilaian.getAlasanPenilaian());
        hasil.setSaranVerifikasi(hasilPenilaian.getSaranVerifikasi());
        hasil.setDaftarDetail(hasilPenilaian.getDetailAnalisisList());

        // 6. Simpan hasil analisis ke database jika DAO aktif
        try {
            analisisDAO.save(hasil);
        } catch (Exception e) {
            logger.warn("Penyimpanan riwayat analisis ke database dilewati: {}", e.getMessage());
        }

        return hasil;
    }

    @Override
    public List<AnalisisTangkapanLayar> getRiwayatAnalisisPengguna(int penggunaId) {
        return analisisDAO.findByPenggunaId(penggunaId);
    }

    private List<IndikatorPenipuan> buatIndikatorDefault() {
        return List.of(
                new IndikatorPenipuan(1, "Permintaan Kode OTP", "(otp|kode verifikasi|one time password)", com.tipuskip.model.TipePencocokan.REGEX, 40, "Meminta kode verifikasi OTP rahasia.", "Jangan bagikan OTP kepada siapa pun.", true),
                new IndikatorPenipuan(2, "Shortlink / Tautan Asing", "(bit\\.ly|s\\.id|tinyurl|t\\.me)", com.tipuskip.model.TipePencocokan.REGEX, 25, "Menggunakan shortlink yang mencurigakan.", "Hindari klik tautan yang disingkat.", true),
                new IndikatorPenipuan(3, "Berkas APK Mencurigakan", "(\\.apk|unduh surat|paket)", com.tipuskip.model.TipePencocokan.REGEX, 45, "Pengiriman file aplikasi Android .apk berbahaya.", "Jangan install aplikasi selain dari toko resmi.", true),
                new IndikatorPenipuan(4, "Urgensi / Tekanan Waktu", "(sekarang juga|dalam 5 menit|segera)", com.tipuskip.model.TipePencocokan.REGEX, 20, "Menciptakan kepanikan dengan batas waktu sempit.", "Tetap tenang dan lakukan kroscek mandiri.", true)
        );
    }
}
