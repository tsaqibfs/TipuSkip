package com.tipuskip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Entry Point Utama Aplikasi Desktop TipuSkip.
 */
public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlUrl = getClass().getResource("/com/tipuskip/view/main.fxml");
            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML main.fxml tidak ditemukan di /com/tipuskip/view/main.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root, 1100, 750);

            // Pasang ikon aplikasi (multi-resolusi untuk taskbar dan window title bar OS)
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

            primaryStage.setTitle("TipuSkip - Edukasi Pencegahan Penipuan Digital (PBO)");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(960);
            primaryStage.setMinHeight(650);
            primaryStage.show();

            logger.info("Aplikasi TipuSkip berhasil dijalankan dengan ikon aplikasi resmi.");
        } catch (Exception e) {
            logger.error("Gagal memulai aplikasi TipuSkip: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
