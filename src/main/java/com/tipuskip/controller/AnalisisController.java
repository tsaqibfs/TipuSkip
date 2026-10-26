package com.tipuskip.controller;

import com.tipuskip.model.AnalisisTangkapanLayar;
import com.tipuskip.model.DetailAnalisis;
import com.tipuskip.model.TingkatRisikoAnalisis;
import com.tipuskip.service.IAnalisisService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.FileValidator;
import com.tipuskip.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controller untuk Analisis Tangkapan Layar (OCR & Deteksi Risiko Penipuan).
 * Bagian Tanggung Jawab Anggota 3 & Anggota 1.
 */
public class AnalisisController {

    @FXML private Button btnPilihGambar;
    @FXML private Button btnMulaiAnalisis;
    @FXML private Label lblNamaFile;
    @FXML private Label lblStatusProses;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView imgPreview;

    @FXML private VBox resultSection;
    @FXML private Label lblBadgeRisiko;
    @FXML private Label lblSkorRisiko;
    @FXML private Label lblDisclaimer;
    @FXML private TextArea txtOcrExtracted;

    @FXML private TableView<DetailAnalisis> tableIndikatorCocok;
    @FXML private TableColumn<DetailAnalisis, String> colIndikatorNama;
    @FXML private TableColumn<DetailAnalisis, String> colIndikatorTeks;
    @FXML private TableColumn<DetailAnalisis, Integer> colIndikatorBobot;

    @FXML private Label lblAlasanPenilaian;
    @FXML private Label lblSaranVerifikasi;

    private File selectedFile;
    private IAnalisisService analisisService;

    @FXML
    public void initialize() {
        analisisService = ServiceFactory.getAnalisisService();

        // Setup Tabel Indikator Cocok
        colIndikatorNama.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIndikator() != null ? data.getValue().getIndikator().getNamaIndikator() : "Indikator #" + data.getValue().getIndikatorId()
        ));
        colIndikatorTeks.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeksDitemukan()));
        colIndikatorBobot.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBobotTerhitung()).asObject());
    }

    @FXML
    public void handlePilihGambar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Tangkapan Layar Percakapan");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Berkas Gambar (*.png, *.jpg, *.jpeg, *.bmp)", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(btnPilihGambar.getScene().getWindow());
        if (file != null) {
            if (!FileValidator.isValidImage(file)) {
                AlertHelper.showError("Format Tidak Didukung", "Harap pilih berkas gambar yang valid (PNG, JPG, JPEG, BMP, maksimal 10MB).");
                return;
            }

            selectedFile = file;
            lblNamaFile.setText(file.getName() + " (" + (file.length() / 1024) + " KB)");
            try {
                imgPreview.setImage(new Image(file.toURI().toString()));
            } catch (Exception e) {
                imgPreview.setImage(null);
            }
            btnMulaiAnalisis.setDisable(false);
            lblStatusProses.setText("Berkas siap diproses.");
        }
    }

    @FXML
    public void handleMulaiAnalisis() {
        if (selectedFile == null) {
            AlertHelper.showWarning("Peringatan", "Pilih berkas gambar terlebih dahulu.");
            return;
        }

        btnPilihGambar.setDisable(true);
        btnMulaiAnalisis.setDisable(true);
        progressBar.setVisible(true);
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        lblStatusProses.setText("Mengekstrak teks via OCR & mencocokkan indikator penipuan...");
        resultSection.setVisible(false);
        resultSection.setManaged(false);

        int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;

        // Pemrosesan asinkron di Background Task agar UI JavaFX tidak membeku
        Task<AnalisisTangkapanLayar> analysisTask = new Task<>() {
            @Override
            protected AnalisisTangkapanLayar call() throws Exception {
                return analisisService.prosesAnalisis(selectedFile, userId);
            }
        };

        analysisTask.setOnSucceeded(e -> {
            AnalisisTangkapanLayar hasil = analysisTask.getValue();
            tampilkanHasil(hasil);
            selesaiMemproses("Analisis selesai!");
        });

        analysisTask.setOnFailed(e -> {
            Throwable ex = analysisTask.getException();
            selesaiMemproses("Gagal memproses gambar.");
            AlertHelper.showError("Kesalahan Analisis", "Gagal memproses gambar: " + (ex != null ? ex.getMessage() : "Error tidak diketahui"));
        });

        new Thread(analysisTask).start();
    }

    private void tampilkanHasil(AnalisisTangkapanLayar hasil) {
        resultSection.setVisible(true);
        resultSection.setManaged(true);

        txtOcrExtracted.setText(hasil.getTeksEkstraksi() != null && !hasil.getTeksEkstraksi().isBlank()
                ? hasil.getTeksEkstraksi()
                : "[Tidak ada teks yang dapat dibaca dari gambar]");

        // Badge Tingkat Risiko
        lblBadgeRisiko.setText(hasil.getTingkatRisiko().getLabel().toUpperCase());
        lblBadgeRisiko.getStyleClass().removeAll("badge-rendah", "badge-sedang", "badge-tinggi");
        if (hasil.getTingkatRisiko() == TingkatRisikoAnalisis.TINGGI) {
            lblBadgeRisiko.getStyleClass().add("badge-tinggi");
        } else if (hasil.getTingkatRisiko() == TingkatRisikoAnalisis.SEDANG) {
            lblBadgeRisiko.getStyleClass().add("badge-sedang");
        } else {
            lblBadgeRisiko.getStyleClass().add("badge-rendah");
        }

        lblSkorRisiko.setText("Total Skor Indikator: " + hasil.getTotalSkorRisiko());
        lblDisclaimer.setText(hasil.getDisclaimer());

        // Tabel Indikator Cocok
        tableIndikatorCocok.setItems(FXCollections.observableArrayList(hasil.getDaftarDetail()));

        // Alasan & Saran Verifikasi
        lblAlasanPenilaian.setText(hasil.getAlasanPenilaian());
        lblSaranVerifikasi.setText(hasil.getSaranVerifikasi());
    }

    private void selesaiMemproses(String status) {
        Platform.runLater(() -> {
            btnPilihGambar.setDisable(false);
            btnMulaiAnalisis.setDisable(false);
            progressBar.setVisible(false);
            lblStatusProses.setText(status);
        });
    }
}
