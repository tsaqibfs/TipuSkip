package com.tipuskip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

            primaryStage.setTitle("TipuSkip - Edukasi Pencegahan Penipuan Digital (PBO)");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(960);
            primaryStage.setMinHeight(650);
            primaryStage.show();

            logger.info("Aplikasi TipuSkip berhasil dijalankan.");
        } catch (Exception e) {
            logger.error("Gagal memulai aplikasi TipuSkip: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
