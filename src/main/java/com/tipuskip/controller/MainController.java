package com.tipuskip.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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
    public void handleNavRiwayat() {
        loadView("/com/tipuskip/view/riwayat.fxml", btnNavRiwayat);
    }

    @FXML
    public void handleNavAdmin() {
        loadView("/com/tipuskip/view/admin.fxml", btnNavAdmin);
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
        Button[] buttons = {btnNavPesan, btnNavTelepon, btnNavAnalisis, btnNavRiwayat, btnNavAdmin};
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
