package com.tipuskip.controller;

import com.tipuskip.model.AnalisisTangkapanLayar;
import com.tipuskip.model.HasilSimulasi;
import com.tipuskip.service.IAnalisisService;
import com.tipuskip.service.ISimulasiService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.util.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller untuk Menampilkan Riwayat Simulasi & Analisis Screenshot.
 * Bagian Tanggung Jawab Anggota 2 & Anggota 1.
 */
public class RiwayatController {

    // Tabel Riwayat Simulasi
    @FXML private TableView<HasilSimulasi> tableRiwayatSimulasi;
    @FXML private TableColumn<HasilSimulasi, Integer> colSimId;
    @FXML private TableColumn<HasilSimulasi, String> colSimJudul;
    @FXML private TableColumn<HasilSimulasi, Integer> colSimSkor;
    @FXML private TableColumn<HasilSimulasi, String> colSimStatus;
    @FXML private TableColumn<HasilSimulasi, Integer> colSimDurasi;
    @FXML private TableColumn<HasilSimulasi, String> colSimWaktu;

    // Tabel Riwayat Analisis Screenshot
    @FXML private TableView<AnalisisTangkapanLayar> tableRiwayatAnalisis;
    @FXML private TableColumn<AnalisisTangkapanLayar, Integer> colAnaId;
    @FXML private TableColumn<AnalisisTangkapanLayar, String> colAnaNamaFile;
    @FXML private TableColumn<AnalisisTangkapanLayar, String> colAnaRisiko;
    @FXML private TableColumn<AnalisisTangkapanLayar, Integer> colAnaSkor;
    @FXML private TableColumn<AnalisisTangkapanLayar, String> colAnaAlasan;
    @FXML private TableColumn<AnalisisTangkapanLayar, String> colAnaWaktu;

    private ISimulasiService simulasiService;
    private IAnalisisService analisisService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @FXML
    public void initialize() {
        simulasiService = ServiceFactory.getSimulasiService();
        analisisService = ServiceFactory.getAnalisisService();

        setupTabelSimulasi();
        setupTabelAnalisis();

        muatRiwayatSimulasi();
        muatRiwayatAnalisis();
    }

    private void setupTabelSimulasi() {
        colSimId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colSimJudul.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJudulSkenario()));
        colSimSkor.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTotalSkor()).asObject());
        colSimStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatusEvaluasi()));
        colSimDurasi.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getDurasiDetik()).asObject());
        colSimWaktu.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getWaktuSelesai() != null ? d.getValue().getWaktuSelesai().format(formatter) : "-"
        ));
    }

    private void setupTabelAnalisis() {
        colAnaId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colAnaNamaFile.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaFile()));
        colAnaRisiko.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTingkatRisiko().getLabel()));
        colAnaSkor.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getTotalSkorRisiko()).asObject());
        colAnaAlasan.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getAlasanPenilaian() != null ? d.getValue().getAlasanPenilaian().split("\n")[0] : "-"
        ));
        colAnaWaktu.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getCreatedAt() != null ? d.getValue().getCreatedAt().format(formatter) : "-"
        ));
    }

    @FXML
    public void muatRiwayatSimulasi() {
        int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
        List<HasilSimulasi> list = simulasiService.getRiwayatSimulasiPengguna(userId);
        tableRiwayatSimulasi.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void muatRiwayatAnalisis() {
        int userId = SessionManager.isLoggedIn() ? SessionManager.getCurrentUser().getId() : 1;
        List<AnalisisTangkapanLayar> list = analisisService.getRiwayatAnalisisPengguna(userId);
        tableRiwayatAnalisis.setItems(FXCollections.observableArrayList(list));
    }
}
