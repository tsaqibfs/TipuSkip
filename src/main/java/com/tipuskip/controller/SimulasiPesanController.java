package com.tipuskip.controller;

import com.tipuskip.model.*;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller untuk Simulasi Pesan Interaktif (Chat WhatsApp/Telegram style).
 * Menerapkan prinsip PBO (Polimorfisme Simulasi, Enkapsulasi Model).
 * Bagian Tanggung Jawab Anggota 1.
 */
public class SimulasiPesanController {

    @FXML private ComboBox<Skenario> cbSkenario;
    @FXML private Label lblKategori;
    @FXML private Label lblKesulitan;
    @FXML private Label lblDeskripsi;
    @FXML private Label lblProgressLangkah;
    @FXML private Label lblSkorLive;
    @FXML private Label lblSkorStatus;

    @FXML private Label lblAvatarText;
    @FXML private Label lblChatHeaderName;
    @FXML private Label lblChatHeaderStatus;

    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox chatBox;
    @FXML private VBox choiceButtonsBox;
    @FXML private VBox responsesContainer;

    @FXML private VBox feedbackBox;
    @FXML private Label lblFeedbackIcon;
    @FXML private Label lblFeedbackJudul;
    @FXML private Label lblFeedbackDetail;

    @FXML private Button btnMulaiSimulasi;
    @FXML private Button btnReset;

    private ISimulasiService simulasiService;
    private Simulasi activeSimulasi;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        simulasiService = ServiceFactory.getSimulasiService();
        muatDaftarSkenario();

        cbSkenario.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblKategori.setText(newVal.getKategori());
                lblKesulitan.setText(newVal.getTingkatKesulitan().getLabel());
                updateKesulitanBadge(newVal.getTingkatKesulitan());
                lblDeskripsi.setText(newVal.getDeskripsi() != null ? newVal.getDeskripsi() : "");
                lblChatHeaderName.setText(newVal.getJudul());
                lblChatHeaderStatus.setText("Kategori: " + newVal.getKategori() + " • Siap Dimulai");
            }
        });
    }

    private void updateKesulitanBadge(TingkatKesulitan kesulitan) {
        lblKesulitan.getStyleClass().removeAll("badge-rendah", "badge-sedang", "badge-tinggi");
        if (kesulitan == TingkatKesulitan.MUDAH) {
            lblKesulitan.getStyleClass().add("badge-rendah");
        } else if (kesulitan == TingkatKesulitan.SEDANG) {
            lblKesulitan.getStyleClass().add("badge-sedang");
        } else {
            lblKesulitan.getStyleClass().add("badge-tinggi");
        }
    }

    private void muatDaftarSkenario() {
        List<Skenario> skenarios = simulasiService.getDaftarSkenarioBerdasarkanJenis(JenisSkenario.PESAN);
        cbSkenario.setItems(FXCollections.observableArrayList(skenarios));
        if (!skenarios.isEmpty()) {
            cbSkenario.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleMulaiSimulasi() {
        Skenario selected = cbSkenario.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Silakan pilih skenario pesan terlebih dahulu.");
            return;
        }

        activeSimulasi = simulasiService.inisialisasiSimulasi(selected.getId());
        chatBox.getChildren().clear();
        choiceButtonsBox.getChildren().clear();
        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        lblSkorLive.setText("100 / 100");
        lblSkorStatus.setText("🛡️ AMAN");
        lblSkorStatus.setStyle("-fx-text-fill: #15803d; -fx-background-color: #dcfce7;");

        lblChatHeaderName.setText(selected.getJudul());
        lblChatHeaderStatus.setText("Online • Simulasi Aktif");
        lblAvatarText.setText("🚨");

        btnMulaiSimulasi.setDisable(true);
        cbSkenario.setDisable(true);

        tampilkanLangkahSaatIni();
    }

    private void tampilkanLangkahSaatIni() {
        if (activeSimulasi == null || activeSimulasi.isSelesai()) {
            selesaikanSimulasi();
            return;
        }

        LangkahSkenario langkah = activeSimulasi.getLangkahSaatIni();
        if (langkah == null) {
            selesaikanSimulasi();
            return;
        }

        // Perbarui nomor langkah di UI
        int totalLangkah = activeSimulasi.getSkenario().getDaftarLangkah().size();
        lblProgressLangkah.setText("Langkah " + langkah.getNomorLangkah() + " dari " + totalLangkah);

        // Tambahkan balon obrolan masuk dari penipu
        tambahBubbleMasuk(langkah.getNamaPengirim(), langkah.getPesanPengirim());

        // Buat Tombol Pilihan Respons Pengguna
        choiceButtonsBox.getChildren().clear();
        List<PilihanRespons> pilihanList = langkah.getDaftarPilihanRespons();
        for (int i = 0; i < pilihanList.size(); i++) {
            PilihanRespons resp = pilihanList.get(i);
            Button btn = new Button((i + 1) + ". " + resp.getTeksRespons());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setWrapText(true);
            btn.getStyleClass().add("choice-btn");

            btn.setOnAction(e -> handlePilihRespons(resp));
            choiceButtonsBox.getChildren().add(btn);
        }

        scrollToBottom();
    }

    private void handlePilihRespons(PilihanRespons respons) {
        // Tampilkan bubble balasan pengguna
        tambahBubbleKeluar(respons.getTeksRespons());

        // Update Feedback Banner
        feedbackBox.setVisible(true);
        feedbackBox.setManaged(true);

        if (respons.getTingkatRisiko() == TingkatRisiko.AMAN) {
            lblFeedbackIcon.setText("✅");
            feedbackBox.setStyle("-fx-background-color: #dcfce7; -fx-border-color: #86efac; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("Tindakan Sangat Tepat! (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
        } else if (respons.getTingkatRisiko() == TingkatRisiko.WASPADA) {
            lblFeedbackIcon.setText("⚠️");
            feedbackBox.setStyle("-fx-background-color: #fef3c7; -fx-border-color: #fde68a; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("Perlu Waspada! (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400e;");
        } else {
            lblFeedbackIcon.setText("❌");
            feedbackBox.setStyle("-fx-background-color: #fee2e2; -fx-border-color: #fca5a5; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("Tindakan Berbahaya / Terjebak! (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #991b1b;");
        }

        lblFeedbackDetail.setText(respons.getUmpanBalik() + "\nKonsekuensi: " + respons.getKonsekuensi());

        // Jalankan alur langkah selanjutnya di objek Simulasi (Polimorfisme PBO)
        boolean lanjut = activeSimulasi.prosesRespons(respons);
        int skorSekarang = activeSimulasi.hitungSkor();
        lblSkorLive.setText(skorSekarang + " / 100");

        if (skorSekarang >= 80) {
            lblSkorStatus.setText("🛡️ AMAN");
            lblSkorStatus.setStyle("-fx-text-fill: #15803d; -fx-background-color: #dcfce7;");
        } else if (skorSekarang >= 50) {
            lblSkorStatus.setText("⚠️ WASPADA");
            lblSkorStatus.setStyle("-fx-text-fill: #b45309; -fx-background-color: #fef3c7;");
        } else {
            lblSkorStatus.setText("🚨 BERISIKO");
            lblSkorStatus.setStyle("-fx-text-fill: #b91c1c; -fx-background-color: #fee2e2;");
        }

        if (lanjut) {
            tampilkanLangkahSaatIni();
        } else {
            selesaikanSimulasi();
        }
    }

    private void selesaikanSimulasi() {
        choiceButtonsBox.getChildren().clear();
        btnMulaiSimulasi.setDisable(false);
        cbSkenario.setDisable(false);
        lblChatHeaderStatus.setText("Simulasi Telah Selesai");

        int skorAkhir = activeSimulasi != null ? activeSimulasi.hitungSkor() : 0;
        String evaluasi = activeSimulasi != null ? activeSimulasi.evaluasiHasil() : "-";
        String ringkasan = activeSimulasi != null ? activeSimulasi.dapatkanRingkasanPembelajaran() : "";

        // Simpan hasil ke riwayat via ISimulasiService
        if (activeSimulasi != null) {
            int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
            HasilSimulasi hasil = activeSimulasi.buatHasilSimulasi(userId);
            simulasiService.simpanHasilSimulasi(hasil);
        }

        AlertHelper.showInfo("Simulasi Selesai!",
                "Selamat! Anda telah menyelesaikan simulasi pesan.\n\n" +
                "Skor Akhir: " + skorAkhir + " (" + evaluasi + ")\n\n" + ringkasan);
    }

    private void tambahBubbleMasuk(String pengirim, String teks) {
        VBox bubble = new VBox(4);
        bubble.getStyleClass().add("bubble-incoming");

        HBox topRow = new HBox(6);
        Label senderLbl = new Label(pengirim);
        senderLbl.getStyleClass().add("bubble-sender");
        topRow.getChildren().add(senderLbl);

        Label textLbl = new Label(teks);
        textLbl.setWrapText(true);
        textLbl.setStyle("-fx-text-fill: #1e293b; -fx-font-size: 13px;");

        bubble.getChildren().addAll(topRow, textLbl);

        // Jika teks mengandung file APK atau link mencurigakan, tambahkan visual attachment warning box
        if (teks.contains(".apk") || teks.contains(".APK")) {
            HBox attachmentBox = new HBox(8);
            attachmentBox.setAlignment(Pos.CENTER_LEFT);
            attachmentBox.getStyleClass().add("attachment-preview-box");
            Label attIcon = new Label("📦");
            attIcon.setStyle("-fx-font-size: 16px;");
            VBox attDetails = new VBox(2);
            Label attName = new Label("Berkas APK Terdeteksi (Potensi Malware Sniffer)");
            attName.setStyle("-fx-font-weight: bold; -fx-text-fill: #b91c1c; -fx-font-size: 11px;");
            Label attWarning = new Label("Jangan pasang aplikasi di luar Google Play Store!");
            attWarning.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748b;");
            attDetails.getChildren().addAll(attName, attWarning);
            attachmentBox.getChildren().addAll(attIcon, attDetails);
            bubble.getChildren().add(attachmentBox);
        } else if (teks.contains("http://") || teks.contains("https://") || teks.contains("s.id")) {
            HBox linkBox = new HBox(8);
            linkBox.setAlignment(Pos.CENTER_LEFT);
            linkBox.getStyleClass().add("attachment-preview-box");
            Label linkIcon = new Label("🔗");
            linkIcon.setStyle("-fx-font-size: 16px;");
            VBox linkDetails = new VBox(2);
            Label linkName = new Label("Tautan Terdeteksi (Periksa Keaslian Domain!)");
            linkName.setStyle("-fx-font-weight: bold; -fx-text-fill: #b45309; -fx-font-size: 11px;");
            Label linkWarning = new Label("Waspada tautan pendek (shortlink) atau situs kloningan.");
            linkWarning.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748b;");
            linkDetails.getChildren().addAll(linkName, linkWarning);
            linkBox.getChildren().addAll(linkIcon, linkDetails);
            bubble.getChildren().add(linkBox);
        }

        // Waktu pesan
        HBox timeRow = new HBox();
        timeRow.setAlignment(Pos.BOTTOM_RIGHT);
        Label timeLbl = new Label(LocalTime.now().format(timeFormatter));
        timeLbl.getStyleClass().add("bubble-time");
        timeRow.getChildren().add(timeLbl);
        bubble.getChildren().add(timeRow);

        HBox wrapper = new HBox(bubble);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        chatBox.getChildren().add(wrapper);
    }

    private void tambahBubbleKeluar(String teks) {
        VBox bubble = new VBox(4);
        bubble.getStyleClass().add("bubble-outgoing");

        Label textLbl = new Label(teks);
        textLbl.setStyle("-fx-text-fill: #064e3b; -fx-font-size: 13px;");
        textLbl.setWrapText(true);
        bubble.getChildren().add(textLbl);

        // Waktu & Tanda centang terbaca (WhatsApp double tick)
        HBox timeRow = new HBox(4);
        timeRow.setAlignment(Pos.BOTTOM_RIGHT);
        Label timeLbl = new Label(LocalTime.now().format(timeFormatter));
        timeLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #65a30d;");
        Label checkLbl = new Label("✓✓");
        checkLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #0284c7; -fx-font-weight: bold;");
        timeRow.getChildren().addAll(timeLbl, checkLbl);
        bubble.getChildren().add(timeRow);

        HBox wrapper = new HBox(bubble);
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        chatBox.getChildren().add(wrapper);
    }

    private void scrollToBottom() {
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    @FXML
    public void handleReset() {
        activeSimulasi = null;
        chatBox.getChildren().clear();
        choiceButtonsBox.getChildren().clear();
        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        lblSkorLive.setText("100 / 100");
        lblSkorStatus.setText("🛡️ AMAN");
        lblSkorStatus.setStyle("-fx-text-fill: #15803d; -fx-background-color: #dcfce7;");
        lblProgressLangkah.setText("Langkah: -");
        lblChatHeaderStatus.setText("Simulasi Direset • Pilih Skenario");

        btnMulaiSimulasi.setDisable(false);
        cbSkenario.setDisable(false);
    }
}
