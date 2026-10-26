package com.tipuskip.analyzer;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Implementasi Ekstraksi Teks menggunakan library Tess4J (Tesseract OCR).
 * Bagian Tanggung Jawab Anggota 3.
 */
public class TesseractTextExtractor implements ITextExtractor {
    private static final Logger logger = LoggerFactory.getLogger(TesseractTextExtractor.class);
    private final ITesseract tesseract;
    private boolean available;

    public TesseractTextExtractor() {
        this("tessdata", "ind+eng");
    }

    public TesseractTextExtractor(String dataPath, String language) {
        this.tesseract = new Tesseract();
        try {
            File dataDir = new File(dataPath);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            this.tesseract.setDatapath(dataDir.getAbsolutePath());
            this.tesseract.setLanguage(language);
            this.available = true;
            logger.info("Tess4J OCR siap dengan datapath: {} dan bahasa: {}", dataDir.getAbsolutePath(), language);
        } catch (Throwable t) {
            this.available = false;
            logger.warn("Inisialisasi Tess4J gagal (Tesseract native mungkin belum terpasang): {}", t.getMessage());
        }
    }

    @Override
    public String extractText(File imageFile) throws Exception {
        if (imageFile == null || !imageFile.exists()) {
            throw new IllegalArgumentException("Berkas gambar tidak ditemukan.");
        }

        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException | UnsatisfiedLinkError e) {
            logger.error("Gagal melakukan OCR pada berkas {}: {}", imageFile.getName(), e.getMessage());
            throw new Exception("Kegagalan OCR: " + e.getMessage() + ". Pastikan Tesseract OCR dan traineddata telah terkonfigurasi.", e);
        }
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
}
