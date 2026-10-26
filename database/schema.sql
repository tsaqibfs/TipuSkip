-- ==========================================================
-- TipuSkip Database Schema
-- Sesuai PRD TipuSkip - Praktikum PBO
-- ==========================================================

CREATE DATABASE IF NOT EXISTS tipuskip_db;
USE tipuskip_db;

-- 1. Tabel Pengguna
CREATE TABLE IF NOT EXISTS pengguna (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('PENGGUNA', 'ADMIN') NOT NULL DEFAULT 'PENGGUNA',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Tabel Skenario
CREATE TABLE IF NOT EXISTS skenario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    judul VARCHAR(150) NOT NULL,
    deskripsi TEXT,
    jenis ENUM('PESAN', 'TELEPON') NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    tingkat_kesulitan ENUM('MUDAH', 'SEDANG', 'SULIT') NOT NULL DEFAULT 'MUDAH',
    status_aktif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tabel Langkah Skenario
CREATE TABLE IF NOT EXISTS langkah_skenario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    skenario_id INT NOT NULL,
    nomor_langkah INT NOT NULL,
    nama_pengirim VARCHAR(100) NOT NULL,
    pesan_pengirim TEXT NOT NULL,
    tipe_konten VARCHAR(30) DEFAULT 'TEXT',
    FOREIGN KEY (skenario_id) REFERENCES skenario(id) ON DELETE CASCADE,
    INDEX idx_skenario_langkah (skenario_id, nomor_langkah)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Tabel Pilihan Respons
CREATE TABLE IF NOT EXISTS pilihan_respons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    langkah_id INT NOT NULL,
    teks_respons TEXT NOT NULL,
    tingkat_risiko ENUM('AMAN', 'WASPADA', 'BAHAYA') NOT NULL,
    poin_skor INT NOT NULL DEFAULT 0,
    umpan_balik TEXT NOT NULL,
    konsekuensi TEXT NOT NULL,
    langkah_berikutnya INT DEFAULT NULL,
    FOREIGN KEY (langkah_id) REFERENCES langkah_skenario(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Tabel Hasil Simulasi
CREATE TABLE IF NOT EXISTS hasil_simulasi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pengguna_id INT NOT NULL,
    skenario_id INT NOT NULL,
    total_skor INT NOT NULL,
    status_evaluasi VARCHAR(50) NOT NULL,
    durasi_detik INT DEFAULT 0,
    waktu_mulai TIMESTAMP NULL,
    waktu_selesai TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pengguna_id) REFERENCES pengguna(id) ON DELETE CASCADE,
    FOREIGN KEY (skenario_id) REFERENCES skenario(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Tabel Indikator Penipuan
CREATE TABLE IF NOT EXISTS indikator_penipuan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_indikator VARCHAR(100) NOT NULL,
    pola_pencocokan VARCHAR(255) NOT NULL,
    tipe_pencocokan ENUM('KEYWORD', 'REGEX') NOT NULL DEFAULT 'KEYWORD',
    bobot_risiko INT NOT NULL DEFAULT 10,
    deskripsi TEXT NOT NULL,
    saran_verifikasi TEXT NOT NULL,
    status_aktif BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Tabel Analisis Tangkapan Layar
CREATE TABLE IF NOT EXISTS analisis_tangkapan_layar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pengguna_id INT NOT NULL,
    nama_file VARCHAR(255) NOT NULL,
    teks_ekstraksi LONGTEXT,
    tingkat_risiko ENUM('RENDAH', 'SEDANG', 'TINGGI') NOT NULL,
    total_skor_risiko INT NOT NULL DEFAULT 0,
    alasan_penilaian TEXT NOT NULL,
    saran_verifikasi TEXT NOT NULL,
    disclaimer VARCHAR(255) DEFAULT 'Hasil analisis bersifat edukasi awal dan tidak menetapkan secara pasti bahwa suatu pesan atau pengirim adalah penipu.',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pengguna_id) REFERENCES pengguna(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Tabel Detail Analisis (Relasi Indikator yang Cocok)
CREATE TABLE IF NOT EXISTS detail_analisis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    analisis_id INT NOT NULL,
    indikator_id INT NOT NULL,
    teks_ditemukan VARCHAR(255),
    bobot_terhitung INT NOT NULL,
    FOREIGN KEY (analisis_id) REFERENCES analisis_tangkapan_layar(id) ON DELETE CASCADE,
    FOREIGN KEY (indikator_id) REFERENCES indikator_penipuan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
