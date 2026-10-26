package com.tipuskip.controller;

import com.tipuskip.model.*;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.simulation.Simulasi;
import com.tipuskip.util.AlertHelper;
import com.tipuskip.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller untuk Simulasi Panggilan Telepon (Voice Call).
 * Bagian Tanggung Jawab Anggota 1.
 */
public class SimulasiTeleponController {

    @FXML private ComboBox<Skenario> cbSkenario;
    @FXML private Label lblKategori;
    @FXML private Label lblKesulitan;
    @FXML private Label lblDeskripsi;
    @FXML private Label lblSkorLive;

    @FXML private Label lblCallerName;
    @FXML private Label lblCallStatus;
    @FXML private Label lblDialogue;

    @FXML private VBox feedbackBox;
    @FXML private Label lblFeedbackJudul;
    @FXML private Label lblFeedbackDetail;

    @FXML private VBox choiceButtonsBox;
    @FXML private Button btnMulaiPanggilan;
    @FXML private Button btnTutupPanggilan;

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
        lblCallStatus.setText("Status: Panggilan Terhubung (00:03)");
        lblSkorLive.setText("100 / 100");
        feedbackBox.setVisible(false);
        feedbackBox.setManaged(false);

        btnMulaiPanggilan.setDisable(true);
        cbSkenario.setDisable(true);

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
        for (PilihanRespons resp : langkah.getDaftarPilihanRespons()) {
            Button btn = new Button("🗣️ " + resp.getTeksRespons());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setWrapText(true);
            btn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-padding: 10px; -fx-cursor: hand;");

            btn.setOnAction(e -> handlePilihRespons(resp));
            choiceButtonsBox.getChildren().add(btn);
        }
    }

    private void handlePilihRespons(PilihanRespons respons) {
        feedbackBox.setVisible(true);
        feedbackBox.setManaged(true);
        lblFeedbackJudul.setText("Respons Anda: " + respons.getTingkatRisiko().getLabel());
        lblFeedbackDetail.setText(respons.getUmpanBalik() + "\nKonsekuensi: " + respons.getKonsekuensi());

        boolean lanjut = activeSimulasi.prosesRespons(respons);
        lblSkorLive.setText(activeSimulasi.hitungSkor() + " / 100");

        if (lanjut) {
            tampilkanLangkahSaatIni();
        } else {
            selesaikanPanggilan();
        }
    }

    @FXML
    public void handleTutupPanggilan() {
        if (activeSimulasi != null && !activeSimulasi.isSelesai()) {
            selesaikanPanggilan();
        } else {
            lblCallStatus.setText("Status: Panggilan Berakhir");
            btnMulaiPanggilan.setDisable(false);
            cbSkenario.setDisable(false);
            choiceButtonsBox.getChildren().clear();
        }
    }

    private void selesaikanPanggilan() {
        lblCallStatus.setText("Status: Panggilan Selesai");
        choiceButtonsBox.getChildren().clear();
        btnMulaiPanggilan.setDisable(false);
        cbSkenario.setDisable(false);

        int skorAkhir = activeSimulasi != null ? activeSimulasi.hitungSkor() : 0;
        String evaluasi = activeSimulasi != null ? activeSimulasi.evaluasiHasil() : "-";
        String ringkasan = activeSimulasi != null ? activeSimulasi.dapatkanRingkasanPembelajaran() : "";

        if (activeSimulasi != null) {
            int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
            HasilSimulasi hasil = activeSimulasi.buatHasilSimulasi(userId);
            simulasiService.simpanHasilSimulasi(hasil);
        }

        AlertHelper.showInfo("Panggilan Selesai",
                "Evaluasi Simulasi Telepon:\n" +
                "Skor Akhir: " + skorAkhir + " (" + evaluasi + ")\n\n" + ringkasan);
    }
}
