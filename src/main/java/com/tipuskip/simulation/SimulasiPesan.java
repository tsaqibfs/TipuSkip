package com.tipuskip.simulation;

import com.tipuskip.model.LangkahSkenario;
import com.tipuskip.model.PilihanRespons;
import com.tipuskip.model.Skenario;
import com.tipuskip.model.TingkatRisiko;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simulasi Pesan Digital (WhatsApp / SMS / Telegram).
 * Menerapkan prinsip Pewarisan (Inheritance) dari kelas abstrak Simulasi
 * dan Polimorfisme pada penanganan pesan bertahap.
 */
public class SimulasiPesan extends Simulasi {
    private int jumlahKlikLinkBahaya;
    private int jumlahResponAman;

    public SimulasiPesan(Skenario skenario) {
        super(skenario);
        this.jumlahKlikLinkBahaya = 0;
        this.jumlahResponAman = 0;
    }

    @Override
    public void jalankan() {
        this.waktuMulai = LocalDateTime.now();
        this.indeksLangkahSaatIni = 0;
        this.skorTerkumpul = 0;
        this.selesai = false;
    }

    @Override
    public boolean prosesRespons(PilihanRespons respons) {
        if (respons == null || selesai) {
            return false;
        }

        riwayatResponsPengguna.add(respons);
        skorTerkumpul += respons.getPoinSkor();

        if (respons.getTingkatRisiko() == TingkatRisiko.AMAN) {
            jumlahResponAman++;
        } else if (respons.getTingkatRisiko() == TingkatRisiko.BAHAYA) {
            jumlahKlikLinkBahaya++;
        }

        // Branching langkah berikutnya jika ada, atau lanjut ke index berikutnya
        List<LangkahSkenario> langkahList = skenario.getDaftarLangkah();
        if (respons.getLangkahBerikutnya() != null) {
            // Cari index dengan nomor langkah yang cocok
            int targetNomor = respons.getLangkahBerikutnya();
            boolean ditemukan = false;
            for (int i = 0; i < langkahList.size(); i++) {
                if (langkahList.get(i).getNomorLangkah() == targetNomor) {
                    indeksLangkahSaatIni = i;
                    ditemukan = true;
                    break;
                }
            }
            if (!ditemukan) {
                akhiriSimulasi();
                return false;
            }
        } else {
            // Lanjut ke langkah berurutan
            indeksLangkahSaatIni++;
        }

        if (indeksLangkahSaatIni >= langkahList.size()) {
            akhiriSimulasi();
            return false;
        }

        return true;
    }

    private void akhiriSimulasi() {
        this.selesai = true;
        this.waktuSelesai = LocalDateTime.now();
    }

    @Override
    public int hitungSkor() {
        if (riwayatResponsPengguna.isEmpty()) return 0;
        // Rata-rata skor dari respons yang dipilih (skala 0 - 100)
        int total = 0;
        for (PilihanRespons r : riwayatResponsPengguna) {
            total += r.getPoinSkor();
        }
        return Math.round((float) total / riwayatResponsPengguna.size());
    }

    @Override
    public String evaluasiHasil() {
        int skor = hitungSkor();
        if (skor >= 85) {
            return "LULUS_AMAN";
        } else if (skor >= 60) {
            return "PERLU_WASPADA";
        } else {
            return "BERISIKO_TINGGI";
        }
    }

    @Override
    public String dapatkanRingkasanPembelajaran() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hasil Pembelajaran Simulasi Pesan:\n");
        sb.append("- Kategori: ").append(skenario.getKategori()).append("\n");
        sb.append("- Respons Aman: ").append(jumlahResponAman).append(" kali\n");
        sb.append("- Terjebak Risiko: ").append(jumlahKlikLinkBahaya).append(" kali\n");
        sb.append("- Status Akhir: ").append(evaluasiHasil()).append("\n\n");
        sb.append("Tips Pencegahan:\n");
        sb.append("1. Jangan mudah tergiur tawaran pekerjaan instan atau komisi fantastis.\n");
        sb.append("2. Jangan pernah membuka shortlink yang mencurigakan.\n");
        sb.append("3. Tolak segala permintaan uang deposit.");
        return sb.toString();
    }

    public int getJumlahKlikLinkBahaya() {
        return jumlahKlikLinkBahaya;
    }
}
