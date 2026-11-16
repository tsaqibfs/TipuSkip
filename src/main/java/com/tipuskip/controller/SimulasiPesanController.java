package com.tipuskip.controller;

import com.tipuskip.model.*;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
    @FXML private Button btnKirimVideo;

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

        // Jika teks mengandung file APK, tautan, atau video simulasi
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
        } else if (teks.contains(".mp4") || teks.contains(".MP4") || teks.toLowerCase().contains("[video]")) {
            // Video Task Scam Preview Card
            String videoName = "Video_Tugas_Trailer_Film.mp4";
            if (teks.contains("[") && teks.contains("]")) {
                int start = teks.indexOf("[");
                int end = teks.indexOf("]");
                if (end > start) {
                    videoName = teks.substring(start + 1, end);
                }
            }
            final String finalVideoName = videoName;

            VBox videoCard = new VBox(8);
            videoCard.getStyleClass().add("video-preview-card");

            StackPane videoScreen = new StackPane();
            videoScreen.getStyleClass().add("video-screen-mock");

            StackPane playBtn = new StackPane();
            playBtn.getStyleClass().add("video-play-circle");
            Label playIcon = new Label("▶");
            playIcon.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 0 0 0 3px;");
            playBtn.getChildren().add(playIcon);

            Label durationBadge = new Label("00:45");
            durationBadge.getStyleClass().add("video-tag-duration");
            StackPane.setAlignment(durationBadge, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(durationBadge, new Insets(0, 8, 8, 0));

            videoScreen.getChildren().addAll(playBtn, durationBadge);

            Label titleLbl = new Label("📹 " + finalVideoName);
            titleLbl.getStyleClass().add("video-file-info");

            Button watchBtn = new Button("▶ Tonton Video Simulasi");
            watchBtn.setMaxWidth(Double.MAX_VALUE);
            watchBtn.getStyleClass().add("btn-watch-video");
            watchBtn.setOnAction(e -> bukaDialogPemutarVideo(finalVideoName, "00:45",
                    "Penipu mengirimkan cuplikan video YouTube/Trailer untuk menarik minat korban. Setelah korban terpancing menyelesaikan tugas like/subscribe, penipu akan memeras uang deposit puluhan juta!"));
            videoScreen.setOnMouseClicked(e -> watchBtn.fire());

            videoCard.getChildren().addAll(videoScreen, titleLbl, watchBtn);
            bubble.getChildren().add(videoCard);
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
    public void handleKirimVideoAction() {
        if (activeSimulasi == null || activeSimulasi.isSelesai()) {
            AlertHelper.showWarning("Peringatan", "Silakan klik tombol 'Mulai Simulasi' terlebih dahulu sebelum mengirim lampiran video.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>("📹 Rekaman Bukti Layar (Telah Menyukai Video) [0:30]",
                "📹 Rekaman Bukti Layar (Telah Menyukai Video) [0:30]",
                "📹 Video Selfie Wajah & Memegang KTP Asli [0:15] (Sangat Berisiko!)");
        dialog.setTitle("Kirim Lampiran Video");
        dialog.setHeaderText("Simulasi Pengiriman Video ke Obrolan");
        dialog.setContentText("Pilih jenis video yang ingin Anda kirimkan:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            if (selected.contains("Selfie")) {
                tambahBubbleVideoKeluar("Video_Verifikasi_Selfie_KTP.mp4", "00:15", "Halo kak ini video selfie wajah dan KTP saya untuk verifikasi komisi.");
                feedbackBox.setVisible(true);
                feedbackBox.setManaged(true);
                lblFeedbackIcon.setText("❌");
                feedbackBox.setStyle("-fx-background-color: #fee2e2; -fx-border-color: #fca5a5; -fx-border-radius: 8px; -fx-padding: 12px;");
                lblFeedbackJudul.setText("FATAL! Identitas Biometrik Bocor");
                lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #991b1b;");
                lblFeedbackDetail.setText("Sangat fatal! Jangan pernah mengirim rekaman video selfie/wajah bersama KTP ke kontak tidak dikenal. Video ini sering disalahgunakan sindikat untuk membobol pinjaman online (pinjol) ilegal!");
                lblSkorLive.setText("10 / 100");
                lblSkorStatus.setText("🚨 BERISIKO");
                lblSkorStatus.setStyle("-fx-text-fill: #b91c1c; -fx-background-color: #fee2e2;");
            } else {
                tambahBubbleVideoKeluar("Bukti_Rekaman_Layar_Like.mp4", "00:30", "Kak ini rekaman video layar bukti saya sudah like videonya ya.");
                feedbackBox.setVisible(true);
                feedbackBox.setManaged(true);
                lblFeedbackIcon.setText("⚠️");
                feedbackBox.setStyle("-fx-background-color: #fef3c7; -fx-border-color: #fde68a; -fx-border-radius: 8px; -fx-padding: 12px;");
                lblFeedbackJudul.setText("Perangkap Tugas Semu (Task Trap)");
                lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400e;");
                lblFeedbackDetail.setText("Anda baru saja mengirim video bukti like. Penipu biasanya memberi komisi receh Rp20.000 untuk memancing rasa percaya, lalu memaksa Anda deposit jutaan rupiah pada misi berikutnya!");
            }
            scrollToBottom();
        });
    }

    private void tambahBubbleVideoKeluar(String judulVideo, String durasi, String caption) {
        VBox bubble = new VBox(6);
        bubble.getStyleClass().add("bubble-outgoing");

        VBox videoCard = new VBox(6);
        videoCard.getStyleClass().add("video-preview-card");

        StackPane videoScreen = new StackPane();
        videoScreen.getStyleClass().add("video-screen-mock");

        StackPane playBtn = new StackPane();
        playBtn.getStyleClass().add("video-play-circle");
        Label playIcon = new Label("▶");
        playIcon.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 0 0 0 3px;");
        playBtn.getChildren().add(playIcon);

        Label durationBadge = new Label(durasi);
        durationBadge.getStyleClass().add("video-tag-duration");
        StackPane.setAlignment(durationBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(durationBadge, new Insets(0, 8, 8, 0));

        videoScreen.getChildren().addAll(playBtn, durationBadge);

        Label titleLbl = new Label("📹 " + judulVideo);
        titleLbl.getStyleClass().add("video-file-info");

        Button watchBtn = new Button("▶ Putar Video Terkirim");
        watchBtn.setMaxWidth(Double.MAX_VALUE);
        watchBtn.getStyleClass().add("btn-watch-video");
        watchBtn.setOnAction(e -> bukaDialogPemutarVideo(judulVideo, durasi,
                "Ini adalah video yang Anda kirimkan ke penipu. Perhatikan bahwa data/rekaman yang sudah terkirim ke platform chat tidak dapat ditarik kembali!"));
        videoScreen.setOnMouseClicked(e -> watchBtn.fire());

        videoCard.getChildren().addAll(videoScreen, titleLbl, watchBtn);

        Label captionLbl = new Label(caption);
        captionLbl.setStyle("-fx-text-fill: #064e3b; -fx-font-size: 13px;");
        captionLbl.setWrapText(true);

        HBox timeRow = new HBox(4);
        timeRow.setAlignment(Pos.BOTTOM_RIGHT);
        Label timeLbl = new Label(LocalTime.now().format(timeFormatter));
        timeLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #65a30d;");
        Label checkLbl = new Label("✓✓");
        checkLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #0284c7; -fx-font-weight: bold;");
        timeRow.getChildren().addAll(timeLbl, checkLbl);

        bubble.getChildren().addAll(videoCard, captionLbl, timeRow);

        HBox wrapper = new HBox(bubble);
        wrapper.setAlignment(Pos.CENTER_RIGHT);
        chatBox.getChildren().add(wrapper);
    }

    private void bukaDialogPemutarVideo(String judulVideo, String durasi, String konteksEdukasi) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Pemutar Video Simulasi - " + judulVideo);

        VBox root = new VBox(12);
        root.setStyle("-fx-background-color: #0f172a; -fx-padding: 20px;");
        root.setPrefWidth(520);

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        Label icon = new Label("🎬");
        icon.setStyle("-fx-font-size: 18px;");
        Label title = new Label(judulVideo);
        title.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 15px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label badge = new Label("Simulasi Edukasi PBO");
        badge.setStyle("-fx-background-color: #1e293b; -fx-text-fill: #38bdf8; -fx-padding: 3 8; -fx-background-radius: 6; -fx-font-size: 10px;");
        header.getChildren().addAll(icon, title, spacer, badge);

        VBox playerScreen = new VBox(10);
        playerScreen.setAlignment(Pos.CENTER);
        playerScreen.setStyle("-fx-background-color: #020617; -fx-background-radius: 12px; -fx-border-color: #334155; -fx-border-radius: 12px; -fx-padding: 24px; -fx-min-height: 200px;");

        Label videoFrameTitle = new Label("▶ MEMUTAR CUPLIKAN TUGAS RESMI (SIMULASI)");
        videoFrameTitle.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label previewAnimation = new Label("🔴 [LIKE & SUBSCRIBE UNTUK KOMISI RP150.000]");
        previewAnimation.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-color: rgba(239,68,68,0.15); -fx-padding: 8 16; -fx-background-radius: 8;");

        ProgressBar progressBar = new ProgressBar(0.0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: #2563eb;");

        HBox timeRow = new HBox();
        timeRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTimeElapsed = new Label("00:00 / " + durasi);
        lblTimeElapsed.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px; -fx-font-family: 'Consolas', monospace;");
        timeRow.getChildren().add(lblTimeElapsed);

        HBox controls = new HBox(12);
        controls.setAlignment(Pos.CENTER);
        Button btnTogglePlay = new Button("⏸ Pause");
        btnTogglePlay.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        Button btnRestart = new Button("⏮ Ulangi");
        btnRestart.setStyle("-fx-background-color: #334155; -fx-text-fill: #f8fafc; -fx-background-radius: 6; -fx-cursor: hand;");
        controls.getChildren().addAll(btnRestart, btnTogglePlay);

        playerScreen.getChildren().addAll(videoFrameTitle, previewAnimation, progressBar, timeRow, controls);

        final int totalSeconds = 30;
        final int[] currentSec = {0};
        Timeline videoTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (currentSec[0] < totalSeconds) {
                currentSec[0]++;
                double progress = (double) currentSec[0] / totalSeconds;
                progressBar.setProgress(progress);
                lblTimeElapsed.setText(String.format("00:%02d / %s", currentSec[0], durasi));
            } else {
                btnTogglePlay.setText("▶ Putar Ulang");
            }
        }));
        videoTimeline.setCycleCount(Timeline.INDEFINITE);
        videoTimeline.play();

        btnTogglePlay.setOnAction(e -> {
            if (btnTogglePlay.getText().contains("Pause")) {
                videoTimeline.pause();
                btnTogglePlay.setText("▶ Lanjutkan");
            } else if (btnTogglePlay.getText().contains("Putar Ulang")) {
                currentSec[0] = 0;
                progressBar.setProgress(0);
                videoTimeline.playFromStart();
                btnTogglePlay.setText("⏸ Pause");
            } else {
                videoTimeline.play();
                btnTogglePlay.setText("⏸ Pause");
            }
        });

        btnRestart.setOnAction(e -> {
            currentSec[0] = 0;
            progressBar.setProgress(0);
            videoTimeline.playFromStart();
            btnTogglePlay.setText("⏸ Pause");
        });

        modalStage.setOnCloseRequest(e -> videoTimeline.stop());

        VBox eduCard = new VBox(6);
        eduCard.setStyle("-fx-background-color: #1e293b; -fx-border-color: #f59e0b; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12;");
        Label eduTitle = new Label("💡 Analisis Bahaya Modus Video Scam:");
        eduTitle.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold; -fx-font-size: 12px;");
        Label eduDesc = new Label(konteksEdukasi);
        eduDesc.setWrapText(true);
        eduDesc.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 11px;");
        eduCard.getChildren().addAll(eduTitle, eduDesc);

        Button btnClose = new Button("Tutup Pemutar");
        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.getStyleClass().add("btn-secondary");
        btnClose.setOnAction(e -> {
            videoTimeline.stop();
            modalStage.close();
        });

        root.getChildren().addAll(header, playerScreen, eduCard, btnClose);

        Scene scene = new Scene(root);
        modalStage.setScene(scene);
        modalStage.show();
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
