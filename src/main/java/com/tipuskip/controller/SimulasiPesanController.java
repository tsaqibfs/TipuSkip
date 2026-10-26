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

import java.util.List;

/**
 * Controller untuk Simulasi Pesan Interaktif (Chat).
 * Bagian Tanggung Jawab Anggota 1.
 */
public class SimulasiPesanController {

    @FXML private ComboBox<Skenario> cbSkenario;
    @FXML private Label lblKategori;
    @FXML private Label lblKesulitan;
    @FXML private Label lblDeskripsi;
    @FXML private Label lblSkorLive;

    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox chatBox;
    @FXML private VBox choiceButtonsBox;
    @FXML private VBox responsesContainer;

    @FXML private VBox feedbackBox;
    @FXML private Label fxFeedbackJudul;
    @FXML private Label lblFeedbackJudul;
    @FXML private Label lblFeedbackDetail;

    @FXML private Button btnMulaiSimulasi;
    @FXML private Button btnReset;

    private ISimulasiService simulasiService;
    private Simulasi activeSimulasi;

    @FXML
    public void initialize() {
        simulasiService = ServiceFactory.getSimulasiService();
        muatDaftarSkenario();

        cbSkenario.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblKategori.setText("Kategori: " + newVal.getKategori());
                lblKesulitan.setText("Tingkat: " + newVal.getTingkatKesulitan().getLabel());
                lblDeskripsi.setText(newVal.getDeskripsi() != null ? newVal.getDeskripsi() : "");
            }
        });
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

        // Tampilkan Bubble Pesan Masuk dari Penipu
        tambahBubbleMasuk(langkah.getNamaPengirim(), langkah.getPesanPengirim());

        // Buat Tombol Pilihan Respons
        choiceButtonsBox.getChildren().clear();
        List<PilihanRespons> pilihanList = langkah.getDaftarPilihanRespons();
        for (PilihanRespons resp : pilihanList) {
            Button btn = new Button(resp.getTeksRespons());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setWrapText(true);
            btn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-padding: 10px; -fx-cursor: hand;");

            btn.setOnAction(e -> handlePilihRespons(resp));
            choiceButtonsBox.getChildren().add(btn);
        }

        scrollToBottom();
    }

    private void handlePilihRespons(PilihanRespons respons) {
        // Tampilkan bubble balasan pengguna
        tambahBubbleKeluar(respons.getTeksRespons());

        // Tampilkan Feedback & Konsekuensi
        feedbackBox.setVisible(true);
        feedbackBox.setManaged(true);
        lblFeedbackJudul.setText("Umpan Balik: " + respons.getTingkatRisiko().getLabel() + " (+" + respons.getPoinSkor() + " poin)");
        lblFeedbackDetail.setText(respons.getUmpanBalik() + "\nKonsekuensi: " + respons.getKonsekuensi());

        if (respons.getTingkatRisiko() == TingkatRisiko.BAHAYA) {
            feedbackBox.setStyle("-fx-background-color: #fee2e2; -fx-border-color: #fca5a5; -fx-border-radius: 8px; -fx-padding: 12px;");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #991b1b;");
        } else if (respons.getTingkatRisiko() == TingkatRisiko.WASPADA) {
            feedbackBox.setStyle("-fx-background-color: #fef3c7; -fx-border-color: #fde68a; -fx-border-radius: 8px; -fx-padding: 12px;");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #92400e;");
        } else {
            feedbackBox.setStyle("-fx-background-color: #dcfce7; -fx-border-color: #86efac; -fx-border-radius: 8px; -fx-padding: 12px;");
            lblFeedbackJudul.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
        }

        // Jalankan alur langkah selanjutnya di objek Simulasi
        boolean lanjut = activeSimulasi.prosesRespons(respons);
        lblSkorLive.setText(activeSimulasi.hitungSkor() + " / 100");

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

        int skorAkhir = activeSimulasi != null ? activeSimulasi.hitungSkor() : 0;
        String evaluasi = activeSimulasi != null ? activeSimulasi.evaluasiHasil() : "-";
        String ringkasan = activeSimulasi != null ? activeSimulasi.dapatkanRingkasanPembelajaran() : "";

        // Simpan ke riwayat via ISimulasiService
        if (activeSimulasi != null) {
            int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
            HasilSimulasi hasil = activeSimulasi.buatHasilSimulasi(userId);
            simulasiService.simpanHasilSimulasi(hasil);
        }

        AlertHelper.showInfo("Simulasi Selesai!",
                "Selamat! Anda telah menyelesaikan simulasi.\n" +
                "Skor Akhir: " + skorAkhir + " (" + evaluasi + ")\n\n" + ringkasan);
    }

    private void tambahBubbleMasuk(String pengirim, String teks) {
        VBox bubble = new VBox(4);
        bubble.getStyleClass().add("bubble-incoming");
        Label senderLbl = new Label(pengirim);
        senderLbl.getStyleClass().add("bubble-sender");
        Label textLbl = new Label(teks);
        textLbl.setWrapText(true);
        bubble.getChildren().addAll(senderLbl, textLbl);

        HBox wrapper = new HBox(bubble);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        chatBox.getChildren().add(wrapper);
    }

    private void tambahBubbleKeluar(String teks) {
        VBox bubble = new VBox(4);
        bubble.getStyleClass().add("bubble-outgoing");
        Label textLbl = new Label(teks);
        textLbl.setStyle("-fx-text-fill: #ffffff;");
        textLbl.setWrapText(true);
        bubble.getChildren().add(textLbl);

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
        lblSkorLive.setText("0 / 100");
        btnMulaiSimulasi.setDisable(false);
        cbSkenario.setDisable(false);
    }
}
