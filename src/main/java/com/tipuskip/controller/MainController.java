package com.tipuskip.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Controller Utama untuk Navigasi Antarmuka TipuSkip.
 * Bagian Tanggung Jawab Anggota 1.
 */
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private StackPane contentArea;
    @FXML private Button btnNavPesan;
    @FXML private Button btnNavTelepon;
    @FXML private Button btnNavAnalisis;
    @FXML private Button btnNavEdukasi;
    @FXML private Button btnNavRiwayat;
    @FXML private Button btnNavAdmin;

    @FXML
    public void initialize() {
        // Halaman awal default: Simulasi Pesan
        handleNavPesan();
    }

    @FXML
    public void handleNavPesan() {
        loadView("/com/tipuskip/view/simulasi_pesan.fxml", btnNavPesan);
    }

    @FXML
    public void handleNavTelepon() {
        loadView("/com/tipuskip/view/simulasi_telepon.fxml", btnNavTelepon);
    }

    @FXML
    public void handleNavAnalisis() {
        loadView("/com/tipuskip/view/analisis.fxml", btnNavAnalisis);
    }

    @FXML
    public void handleNavEdukasi() {
        loadView("/com/tipuskip/view/edukasi.fxml", btnNavEdukasi);
    }

    @FXML
    public void handleNavRiwayat() {
        loadView("/com/tipuskip/view/riwayat.fxml", btnNavRiwayat);
    }

    @FXML
    public void handleNavAdmin() {
        loadView("/com/tipuskip/view/admin.fxml", btnNavAdmin);
    }

    @FXML
    public void handleTentangAplikasi() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tentang TipuSkip Desktop");
        alert.setHeaderText("🛡️ TipuSkip Desktop Edition (v1.0.0)");
        alert.setContentText(
                "TipuSkip adalah Aplikasi Desktop Edukasi Interaktif untuk Melatih " +
                "Kewaspadaan Masyarakat Terhadap Berbagai Modus Penipuan Digital " +
                "berbasis Pemrograman Berorientasi Objek (PBO).\n\n" +
                "👤 Pembagian Tugas Tim Pengembang (3 Anggota):\n" +
                "• Anggota 1: UI/UX JavaFX, Simulasi Pesan & Telepon, Naskah Skenario, & Modul Edukasi.\n" +
                "• Anggota 2: Basis Data MySQL, JDBC Singleton, Generic DAO Pattern, & Panel Admin.\n" +
                "• Anggota 3: Analisis Tangkapan Layar, OCR Tess4J, Pencocokan Indikator & Risk Engine.\n\n" +
                "⌨️ Pintasan Keyboard Desktop (PBO Desktop Shortcuts):\n" +
                "• [Ctrl + 1] : Buka Simulasi Pesan WhatsApp\n" +
                "• [Ctrl + 2] : Buka Simulasi Telepon & Smart Dialer\n" +
                "• [Ctrl + 3] : Buka Analisis Tangkapan Layar OCR\n" +
                "• [Ctrl + 4] : Buka Modul Edukasi Anti-Scam\n" +
                "• [Ctrl + 5] : Buka Riwayat Aktivitas & Skor\n" +
                "• [Ctrl + 6] : Buka Admin Panel Manajemen Skenario\n" +
                "• [F1]       : Bantuan Cepat Modul Edukasi\n" +
                "• [Ctrl + Q] : Keluar dari Aplikasi Desktop\n\n" +
                "© 2026 Tim TipuSkip. Hak Cipta Dilindungi Undang-Undang."
        );

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            URL iconUrl = getClass().getResource("/com/tipuskip/images/app-icon-64.png");
            if (iconUrl != null) {
                stage.getIcons().add(new Image(iconUrl.toExternalForm()));
            }
        } catch (Exception e) {
            logger.debug("Gagal memasang icon pada dialog info: {}", e.getMessage());
        }

        alert.showAndWait();
    }

    private void loadView(String fxmlPath, Button activeButton) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                logger.error("File FXML tidak ditemukan: {}", fxmlPath);
                return;
            }
            Node node = FXMLLoader.load(resource);
            contentArea.getChildren().setAll(node);
            setActiveNavButton(activeButton);
        } catch (IOException e) {
            logger.error("Gagal memuat view {}: {}", fxmlPath, e.getMessage(), e);
        }
    }

    private void setActiveNavButton(Button active) {
        Button[] buttons = {btnNavPesan, btnNavTelepon, btnNavAnalisis, btnNavEdukasi, btnNavRiwayat, btnNavAdmin};
        for (Button b : buttons) {
            if (b != null) {
                b.getStyleClass().remove("nav-button-active");
            }
        }
        if (active != null) {
            active.getStyleClass().add("nav-button-active");
        }
    }
}
