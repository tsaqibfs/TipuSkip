package com.tipuskip.controller;

import com.tipuskip.model.*;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller untuk Simulasi Telepon Interaktif (Smartphone UI & Interactive Dialer)
 * dengan dukungan Tema Multi-Platform (GSM/Truecaller, WhatsApp Call, Instagram Audio, Messenger, TikTok).
 * Menerapkan prinsip PBO (Polimorfisme Simulasi, Enkapsulasi Model).
 * Bagian Tanggung Jawab Anggota 1.
 */
public class SimulasiTeleponController {

    public enum PhoneTheme {
        GSM("📱 Telepon Seluler (GSM / Truecaller)", "phone-theme-gsm", "📶 5G • GSM", "📞",
                "⚠️ Dilaporkan Komunitas: Potensi Penipuan (Spam OTP)", "Panggilan Seluler Masuk"),
        WHATSAPP("🟢 Panggilan Suara WhatsApp", "phone-theme-whatsapp", "📶 WiFi • WhatsApp Call", "🟢",
                "⚠️ WhatsApp Trust: Panggilan dari Nomor Bisnis Mencurigakan", "Panggilan Suara WhatsApp"),
        INSTAGRAM("📸 Panggilan Audio Instagram", "phone-theme-instagram", "📶 WiFi • Instagram Direct", "📸",
                "⚠️ Instagram Safety: Permintaan Panggilan Akun Tak Dikenal", "Instagram Audio Call"),
        MESSENGER("💬 Panggilan Suara Messenger", "phone-theme-messenger", "📶 5G • Messenger Call", "💬",
                "⚠️ Messenger Security: Kontak Belum Pernah Berinteraksi", "Panggilan Suara Messenger"),
        TIKTOK("🎵 Panggilan Langsung TikTok", "phone-theme-tiktok", "📶 5G • TikTok Live Audio", "🎵",
                "⚠️ TikTok Anti-Scam: Pengguna Terindikasi Penipuan Tugas", "Panggilan Langsung TikTok");

        private final String label;
        private final String cssClass;
        private final String networkStatus;
        private final String avatarIcon;
        private final String spamBadgeText;
        private final String callTitle;

        PhoneTheme(String label, String cssClass, String networkStatus, String avatarIcon, String spamBadgeText, String callTitle) {
            this.label = label;
            this.cssClass = cssClass;
            this.networkStatus = networkStatus;
            this.avatarIcon = avatarIcon;
            this.spamBadgeText = spamBadgeText;
            this.callTitle = callTitle;
        }

        public String getLabel() { return label; }
        public String getCssClass() { return cssClass; }
        public String getNetworkStatus() { return networkStatus; }
        public String getAvatarIcon() { return avatarIcon; }
        public String getSpamBadgeText() { return spamBadgeText; }
        public String getCallTitle() { return callTitle; }

        public static PhoneTheme fromLabel(String label) {
            for (PhoneTheme theme : values()) {
                if (theme.label.equals(label)) return theme;
            }
            return GSM;
        }
    }

    @FXML private ComboBox<String> cbPhonePlatform;
    @FXML private ComboBox<Skenario> cbSkenario;
    @FXML private Label lblKategori;
    @FXML private Label lblKesulitan;
    @FXML private Label lblDeskripsi;
    @FXML private Label lblSkorLive;
    @FXML private Label lblSkorStatus;

    @FXML private VBox phoneContainer;
    @FXML private VBox phoneScreen;
    @FXML private Label lblPhoneClock;
    @FXML private Label lblPhoneNetwork;

    @FXML private StackPane avatarContainer;
    @FXML private Label lblAvatarIcon;
    @FXML private Label lblCallerName;
    @FXML private Label lblCallerNumber;
    @FXML private Label lblSpamBadge;
    @FXML private Label lblCallStatus;
    @FXML private Label lblCallTimer;
    @FXML private Label lblDialogue;

    @FXML private Button btnAnswer;
    @FXML private Button btnHangup;
    @FXML private Label lblDialerDisplay;
    @FXML private Button btnBackspace;
    @FXML private Button btnPanggilNomor;

    @FXML private VBox feedbackBox;
    @FXML private Label lblFeedbackIcon;
    @FXML private Label lblFeedbackJudul;
    @FXML private Label lblFeedbackDetail;

    @FXML private VBox choiceButtonsBox;
    @FXML private Button btnMulaiPanggilan;
    @FXML private Button btnTutupPanggilan;

    private ISimulasiService simulasiService;
    private Simulasi activeSimulasi;
    private PhoneTheme currentPhoneTheme = PhoneTheme.GSM;

    // Call Duration Timer
    private Timeline callTimerTimeline;
    private int callSecondsElapsed = 0;
    private boolean isCallActive = false;

    @FXML
    public void initialize() {
        simulasiService = ServiceFactory.getSimulasiService();
        muatDaftarSkenario();
        inisialisasiPlatformSwitcher();

        cbSkenario.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblKategori.setText(newVal.getKategori());
                lblKesulitan.setText(newVal.getTingkatKesulitan().getLabel());
                updateKesulitanBadge(newVal.getTingkatKesulitan());
                lblDeskripsi.setText(newVal.getDeskripsi() != null ? newVal.getDeskripsi() : "");
                lblCallerName.setText(newVal.getJudul());
                lblCallStatus.setText("Status: Panggilan Siap Dijalankan (" + currentPhoneTheme.getCallTitle() + ")");
            }
        });

        initTimer();
        updateClock();
    }

    private void inisialisasiPlatformSwitcher() {
        if (cbPhonePlatform != null) {
            for (PhoneTheme theme : PhoneTheme.values()) {
                cbPhonePlatform.getItems().add(theme.getLabel());
            }
            cbPhonePlatform.getSelectionModel().selectFirst();
            cbPhonePlatform.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    terapkanTemaPlatform(PhoneTheme.fromLabel(newVal));
                }
            });
        }
    }

    private void terapkanTemaPlatform(PhoneTheme theme) {
        this.currentPhoneTheme = theme;

        if (phoneContainer != null) {
            phoneContainer.getStyleClass().removeAll(
                    "phone-theme-gsm", "phone-theme-whatsapp", "phone-theme-instagram", "phone-theme-messenger", "phone-theme-tiktok"
            );
            phoneContainer.getStyleClass().add(theme.getCssClass());
        }

        if (lblPhoneNetwork != null) {
            lblPhoneNetwork.setText(theme.getNetworkStatus());
        }

        if (lblSpamBadge != null) {
            lblSpamBadge.setText(theme.getSpamBadgeText());
        }

        if (avatarContainer != null) {
            avatarContainer.getStyleClass().removeAll(
                    "avatar-whatsapp", "avatar-instagram-ring", "avatar-messenger", "avatar-tiktok"
            );
            if (theme == PhoneTheme.INSTAGRAM) {
                avatarContainer.getStyleClass().add("avatar-instagram-ring");
            } else if (theme == PhoneTheme.MESSENGER) {
                avatarContainer.getStyleClass().add("avatar-messenger");
            } else if (theme == PhoneTheme.TIKTOK) {
                avatarContainer.getStyleClass().add("avatar-tiktok");
            } else if (theme == PhoneTheme.WHATSAPP) {
                avatarContainer.getStyleClass().add("avatar-whatsapp");
            }
        }

        if (!isCallActive && lblAvatarIcon != null) {
            lblAvatarIcon.setText(theme.getAvatarIcon());
        }
    }

    private void updateClock() {
        if (lblPhoneClock != null) {
            lblPhoneClock.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void initTimer() {
        callTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            callSecondsElapsed++;
            int mins = callSecondsElapsed / 60;
            int secs = callSecondsElapsed % 60;
            lblCallTimer.setText(String.format("%02d:%02d", mins, secs));
        }));
        callTimerTimeline.setCycleCount(Timeline.INDEFINITE);
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
        List<Skenario> skenarios = simulasiService.getDaftarSkenarioBerdasarkanJenis(JenisSkenario.TELEPON);
        cbSkenario.setItems(FXCollections.observableArrayList(skenarios));
        if (!skenarios.isEmpty()) {
            cbSkenario.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void handleMulaiPanggilan() {
        Skenario selected = cbSkenario.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Pilih skenario telepon terlebih dahulu.");
            return;
        }

        activeSimulasi = simulasiService.inisialisasiSimulasi(selected.getId());
        lblSkorLive.setText("100 / 100");
        lblSkorStatus.setText("🛡️ AMAN");
        lblSkorStatus.setStyle("-fx-text-fill: #15803d; -fx-background-color: #dcfce7;");

        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        btnMulaiPanggilan.setDisable(true);
        cbSkenario.setDisable(true);

        isCallActive = false;
        callSecondsElapsed = 0;
        lblCallTimer.setText("00:00");
        lblCallStatus.setText("Status: " + currentPhoneTheme.getCallTitle() + " Berdering... 📳");
        avatarContainer.getStyleClass().add("phone-avatar-ringing");
        lblAvatarIcon.setText("📳");

        LangkahSkenario langkahAwal = activeSimulasi.getLangkahSaatIni();
        if (langkahAwal != null) {
            lblCallerName.setText(langkahAwal.getNamaPengirim());
            lblCallerNumber.setText("+62-812-9988-" + (1000 + selected.getId() * 111));
            lblDialogue.setText("\"Panggilan masuk sedang berdering... Tekan tombol 'Angkat' (tombol hijau) untuk menjawab panggilan atau 'Tolak' untuk mengabaikan.\"");
        }

        choiceButtonsBox.getChildren().clear();
        btnAnswer.setDisable(false);
    }

    @FXML
    public void handleAnswerCall() {
        if (activeSimulasi == null || activeSimulasi.isSelesai()) {
            return;
        }

        isCallActive = true;
        callSecondsElapsed = 0;
        callTimerTimeline.playFromStart();

        avatarContainer.getStyleClass().remove("phone-avatar-ringing");
        lblAvatarIcon.setText("🗣️");
        lblCallStatus.setText("Status: Panggilan Terhubung (" + currentPhoneTheme.getCallTitle() + ")");
        btnAnswer.setDisable(true);

        tampilkanLangkahSaatIni();
    }

    private void tampilkanLangkahSaatIni() {
        if (activeSimulasi == null || activeSimulasi.isSelesai()) {
            selesaikanPanggilan();
            return;
        }

        LangkahSkenario langkah = activeSimulasi.getLangkahSaatIni();
        if (langkah == null) {
            selesaikanPanggilan();
            return;
        }

        lblCallerName.setText(langkah.getNamaPengirim());
        lblDialogue.setText("\"" + langkah.getPesanPengirim() + "\"");

        choiceButtonsBox.getChildren().clear();
        for (int i = 0; i < langkah.getDaftarPilihanRespons().size(); i++) {
            PilihanRespons resp = langkah.getDaftarPilihanRespons().get(i);
            Button btn = new Button("🗣️ " + (i + 1) + ". " + resp.getTeksRespons());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setWrapText(true);
            btn.getStyleClass().add("choice-btn");

            btn.setOnAction(e -> handlePilihRespons(resp));
            choiceButtonsBox.getChildren().add(btn);
        }
    }

    private void handlePilihRespons(PilihanRespons respons) {
        feedbackBox.setVisible(true);
        feedbackBox.setManaged(true);

        if (respons.getTingkatRisiko() == TingkatRisiko.AMAN) {
            lblFeedbackIcon.setText("✅");
            feedbackBox.setStyle("-fx-background-color: #dcfce7; -fx-border-color: #86efac; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("Keputusan Sangat Bijak! (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
        } else if (respons.getTingkatRisiko() == TingkatRisiko.WASPADA) {
            lblFeedbackIcon.setText("⚠️");
            feedbackBox.setStyle("-fx-background-color: #fef3c7; -fx-border-color: #fde68a; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("Reaksi Panik / Berisiko (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400e;");
        } else {
            lblFeedbackIcon.setText("❌");
            feedbackBox.setStyle("-fx-background-color: #fee2e2; -fx-border-color: #fca5a5; -fx-border-radius: 8px; -fx-background-radius: 8px;");
            lblFeedbackJudul.setText("FATAL! Kredensial Bocor (+ " + respons.getPoinSkor() + " poin)");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #991b1b;");
        }

        lblFeedbackDetail.setText(respons.getUmpanBalik() + "\nKonsekuensi: " + respons.getKonsekuensi());

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
            selesaikanPanggilan();
        }
    }

    @FXML
    public void handleTutupPanggilan() {
        if (callTimerTimeline != null) {
            callTimerTimeline.stop();
        }
        isCallActive = false;

        avatarContainer.getStyleClass().remove("phone-avatar-ringing");
        lblAvatarIcon.setText("📵");
        lblCallStatus.setText("Status: Panggilan Diakhiri / Ditolak");

        if (activeSimulasi != null && !activeSimulasi.isSelesai()) {
            selesaikanPanggilan();
        } else {
            btnMulaiPanggilan.setDisable(false);
            cbSkenario.setDisable(false);
            choiceButtonsBox.getChildren().clear();
        }
    }

    private void selesaikanPanggilan() {
        if (callTimerTimeline != null) {
            callTimerTimeline.stop();
        }
        isCallActive = false;

        lblCallStatus.setText("Status: Panggilan Selesai");
        lblAvatarIcon.setText(currentPhoneTheme.getAvatarIcon());
        choiceButtonsBox.getChildren().clear();
        btnMulaiPanggilan.setDisable(false);
        cbSkenario.setDisable(false);
        btnAnswer.setDisable(true);

        int skorAkhir = activeSimulasi != null ? activeSimulasi.hitungSkor() : 0;
        String evaluasi = activeSimulasi != null ? activeSimulasi.evaluasiHasil() : "-";
        String ringkasan = activeSimulasi != null ? activeSimulasi.dapatkanRingkasanPembelajaran() : "";

        if (activeSimulasi != null) {
            int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
            HasilSimulasi hasil = activeSimulasi.buatHasilSimulasi(userId);
            simulasiService.simpanHasilSimulasi(hasil);
        }

        AlertHelper.showInfo("Simulasi Panggilan Selesai",
                "Evaluasi Simulasi Telepon (" + currentPhoneTheme.getLabel() + "):\n\n" +
                "Skor Akhir: " + skorAkhir + " (" + evaluasi + ")\n\n" + ringkasan);
    }

    // =========================================================================
    // Phone Keypad Dialer Event Handlers
    // =========================================================================

    @FXML
    public void handleDialerKey(ActionEvent event) {
        if (event.getSource() instanceof Button btn) {
            String key = btn.getText();
            String current = lblDialerDisplay.getText();
            if (current.length() < 16) {
                lblDialerDisplay.setText(current + key);
            }
        }
    }

    @FXML
    public void handleDialerBackspace() {
        String current = lblDialerDisplay.getText();
        if (!current.isEmpty()) {
            lblDialerDisplay.setText(current.substring(0, current.length() - 1));
        }
    }

    @FXML
    public void handleDialEnteredNumber() {
        String number = lblDialerDisplay.getText().trim();
        if (number.isEmpty()) {
            AlertHelper.showWarning("Dialer Kosong", "Silakan tekan angka pada keypad terlebih dahulu.");
            return;
        }

        if (isCallActive) {
            AlertHelper.showInfo("DTMF Keypad Terkirim", "Anda menekan nada keypad [" + number + "] ke sambungan telepon penipu.");
        } else {
            if (number.equals("1500888") || number.equals("14000") || number.equals("1500017") || number.equals("110")) {
                AlertHelper.showInfo("Panggilan Call Center Resmi",
                        "Anda mendial nomor resmi [" + number + "].\n" +
                        "Ini adalah nomor resmi bank/instansi. Verifikasi dua arah berhasil dan aman!");
            } else {
                AlertHelper.showInfo("Panggilan Keluar", "Memanggil nomor: " + number + "...\n(Pastikan memverifikasi nomor resmi melalui website resmi instansi).");
            }
        }
    }
}
