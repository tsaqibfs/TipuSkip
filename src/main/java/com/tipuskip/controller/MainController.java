package com.tipuskip.controller;

import com.tipuskip.MainApp;
import com.tipuskip.dao.PenggunaDAO;
import com.tipuskip.dao.impl.PenggunaDAOImpl;
import com.tipuskip.model.Pengguna;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Controller Utama untuk Navigasi Antarmuka TipuSkip.
 * Bagian Tanggung Jawab Anggota 1.
 */
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private HBox customTitleBar;
    @FXML private Button btnWinMinimize;
    @FXML private Button btnWinMaximize;
    @FXML private Button btnWinClose;

    @FXML private StackPane contentArea;
    @FXML private Button btnNavPesan;
    @FXML private Button btnNavTelepon;
    @FXML private Button btnNavAnalisis;
    @FXML private Button btnNavEdukasi;
    @FXML private Button btnNavRiwayat;
    @FXML private Button btnNavAdmin;

    private Stage primaryStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public void setupCustomTitleBar(Stage stage) {
        this.primaryStage = stage;

        if (customTitleBar != null) {
            customTitleBar.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            customTitleBar.setOnMouseDragged(event -> {
                if (!primaryStage.isMaximized()) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            customTitleBar.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    handleWindowMaximize();
                }
            });
        }
    }

    @FXML
    public void handleWindowMinimize() {
        if (primaryStage != null) {
            primaryStage.setIconified(true);
        }
    }

    @FXML
    public void handleWindowMaximize() {
        if (primaryStage != null) {
            boolean isMax = !primaryStage.isMaximized();
            primaryStage.setMaximized(isMax);
            if (btnWinMaximize != null) {
                btnWinMaximize.setText(isMax ? "❐" : "▢");
            }
        }
    }

    @FXML
    public void handleWindowClose() {
        if (primaryStage != null) {
            MainApp.mintaKonfirmasiKeluar(primaryStage);
        }
    }

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
        if (!SessionManager.isAdmin()) {
            boolean terautentikasi = tampilkanDialogLoginAdmin();
            if (!terautentikasi) {
                return;
            }
        }
        loadView("/com/tipuskip/view/admin.fxml", btnNavAdmin);
    }

    private boolean tampilkanDialogLoginAdmin() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("🔐 Autentikasi Administrator");
        dialog.setHeaderText("Akses Terbatas Administrator (FR-12 & FR-13)\nMasukkan kredensial admin (Username: admin | Password: admin / admin123):");

        ButtonType btnLoginType = new ButtonType("Masuk", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnLoginType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField txtUsername = new TextField("admin");
        txtUsername.setPromptText("Username Admin");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password Admin");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(txtPassword, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == btnLoginType) {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                AlertHelper.showWarning("Peringatan", "Username dan password tidak boleh kosong!");
                return false;
            }

            PenggunaDAO penggunaDAO = new PenggunaDAOImpl();
            Optional<Pengguna> auth = penggunaDAO.authenticate(username, password);

            if (auth.isPresent() && auth.get().isAdmin()) {
                SessionManager.setCurrentUser(auth.get());
                AlertHelper.showInfo("Akses Diterima", "Selamat datang kembali, Administrator " + auth.get().getUsername() + "!");
                return true;
            } else {
                AlertHelper.showError("Akses Ditolak", "Username atau Password salah, atau akun ini tidak memiliki peran sebagai Administrator.");
                return false;
            }
        }
        return false;
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
