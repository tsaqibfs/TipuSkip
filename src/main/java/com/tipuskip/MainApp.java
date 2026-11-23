package com.tipuskip;

import com.tipuskip.controller.MainController;
import com.tipuskip.util.WindowResizeHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;

/**
 * Entry Point Utama Aplikasi Desktop TipuSkip.
 * Menerapkan standar desktop: window title bar icon, multi-resolution taskbar icon,
 * global keyboard accelerators, footer status bar, dan konfirmasi penutupan jendela.
 */
public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inisialisasi Custom Integrated Title Bar (Undecorated Stage tanpa bingkai OS)
            primaryStage.initStyle(StageStyle.UNDECORATED);

            URL fxmlUrl = getClass().getResource("/com/tipuskip/view/main.fxml");
            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML main.fxml tidak ditemukan di /com/tipuskip/view/main.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            MainController controller = loader.getController();

            // Deteksi ukuran layar visual pengguna (memperhitungkan taskbar & DPI scaling laptop)
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = visualBounds.getWidth();
            double screenHeight = visualBounds.getHeight();

            // Hitung ukuran jendela yang proporsional dan pas di layar tanpa kebesaran (default ~980x620)
            double initialWidth = Math.min(990, Math.max(840, screenWidth * 0.80));
            double initialHeight = Math.min(630, Math.max(500, screenHeight * 0.84));

            if (initialHeight > screenHeight - 30) {
                initialHeight = screenHeight - 40;
            }
            if (initialWidth > screenWidth - 30) {
                initialWidth = screenWidth - 40;
            }

            Scene scene = new Scene(root, initialWidth, initialHeight);

            // Hubungkan Title Bar Kustom dengan Stage
            if (controller != null) {
                controller.setupCustomTitleBar(primaryStage);
            }

            // Aktifkan resize helper pada window border
            WindowResizeHelper.attach(primaryStage, scene);

            // 1. Pasang ikon aplikasi (multi-resolusi untuk taskbar dan window title bar OS)
            String[] iconSizes = {"16", "32", "64", "128", "256"};
            for (String size : iconSizes) {
                URL iconUrl = getClass().getResource("/com/tipuskip/images/app-icon-" + size + ".png");
                if (iconUrl != null) {
                    primaryStage.getIcons().add(new Image(iconUrl.toExternalForm()));
                }
            }
            URL defaultIconUrl = getClass().getResource("/com/tipuskip/images/app-icon.png");
            if (defaultIconUrl != null) {
                primaryStage.getIcons().add(new Image(defaultIconUrl.toExternalForm()));
            }

            // 2. Daftarkan Keyboard Accelerators khas aplikasi desktop
            if (controller != null) {
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN), controller::handleNavPesan);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN), controller::handleNavTelepon);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN), controller::handleNavAnalisis);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN), controller::handleNavEdukasi);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.CONTROL_DOWN), controller::handleNavRiwayat);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.CONTROL_DOWN), controller::handleNavAdmin);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F1), controller::handleNavEdukasi);
                scene.getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN), () -> mintaKonfirmasiKeluar(primaryStage));
            }

            // 3. Konfirmasi keluar standar aplikasi desktop saat tombol silang [X] diklik
            primaryStage.setOnCloseRequest(event -> {
                event.consume(); // batalkan penutupan langsung
                mintaKonfirmasiKeluar(primaryStage);
            });

            primaryStage.setTitle("TipuSkip - Edukasi Pencegahan Penipuan Digital (Desktop App)");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(780);
            primaryStage.setMinHeight(480);
            primaryStage.setWidth(initialWidth);
            primaryStage.setHeight(initialHeight);
            primaryStage.centerOnScreen();
            primaryStage.show();

            logger.info("Aplikasi Desktop TipuSkip berhasil dijalankan.");
        } catch (Exception e) {
            logger.error("Gagal memulai aplikasi TipuSkip: {}", e.getMessage(), e);
        }
    }

    public static void mintaKonfirmasiKeluar(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Keluar TipuSkip");
        alert.setHeaderText("Keluar dari Aplikasi TipuSkip?");
        alert.setContentText("Apakah Anda yakin ingin menutup aplikasi edukasi TipuSkip?");

        try {
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            if (!stage.getIcons().isEmpty()) {
                alertStage.getIcons().add(stage.getIcons().get(0));
            }
        } catch (Exception ignored) {}

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage.close();
            Platform.exit();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
