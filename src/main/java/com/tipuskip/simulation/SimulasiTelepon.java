package com.tipuskip.simulation;

import com.tipuskip.model.LangkahSkenario;
import com.tipuskip.model.PilihanRespons;
import com.tipuskip.model.Skenario;
import com.tipuskip.model.TingkatRisiko;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Simulasi Panggilan Telepon Palsu (Impersonasi Bank / Lembaga Resmi).
 * Menerapkan prinsip Pewarisan (Inheritance) dari kelas abstrak Simulasi
 * dan Polimorfisme pada dinamika dialog panggilan suara.
 */
public class SimulasiTelepon extends Simulasi {
    private boolean otpHampirBocor;
    private boolean berhasilVerifikasiMandiri;

    public SimulasiTelepon(Skenario skenario) {
        super(skenario);
        this.otpHampirBocor = false;
        this.berhasilVerifikasiMandiri = false;
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

        if (respons.getTingkatRisiko() == TingkatRisiko.BAHAYA) {
            otpHampirBocor = true;
        } else if (respons.getTeksRespons().toLowerCase().contains("mandiri") ||
                   respons.getTeksRespons().toLowerCase().contains("call center")) {
            berhasilVerifikasiMandiri = true;
        }

        List<LangkahSkenario> langkahList = skenario.getDaftarLangkah();
        if (respons.getLangkahBerikutnya() != null) {
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
        int total = 0;
        for (PilihanRespons r : riwayatResponsPengguna) {
            total += r.getPoinSkor();
        }
        int rataRata = Math.round((float) total / riwayatResponsPengguna.size());
        // Jika membocorkan OTP, skor dipenalti
        if (otpHampirBocor) {
            rataRata = Math.min(rataRata, 40);
        }
        return rataRata;
    }

    @Override
    public String evaluasiHasil() {
        int skor = hitungSkor();
        if (skor >= 80) {
            return "LULUS_AMAN";
        } else if (skor >= 50) {
            return "PERLU_WASPADA";
        } else {
            return "BERISIKO_TINGGI";
        }
    }

    @Override
    public String dapatkanRingkasanPembelajaran() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hasil Pembelajaran Simulasi Panggilan Telepon:\n");
        sb.append("- Kategori: ").append(skenario.getKategori()).append("\n");
        sb.append("- Kebocoran Kredensial: ").append(otpHampirBocor ? "YA (Bahaya!)" : "TIDAK (Aman)").append("\n");
        sb.append("- Inisiatif Verifikasi Mandiri: ").append(berhasilVerifikasiMandiri ? "YA (Hebat)" : "BELUM").append("\n");
        sb.append("- Status Akhir: ").append(evaluasiHasil()).append("\n\n");
        sb.append("Tips Penting Menghadapi Panggilan Mencurigakan:\n");
        sb.append("1. Pihak bank/resmi TIDAK PERNAH meminta kode OTP, PIN, atau 3 digit CVV di belakang kartu.\n");
        sb.append("2. Segera tutup panggilan dan hubungi nomor resmi bank yang tercantum di balik kartu ATM.");
        return sb.toString();
    }
}
