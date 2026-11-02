package com.tipuskip.controller;

import com.tipuskip.model.*;
import com.tipuskip.service.IAdminService;
import com.tipuskip.service.ServiceFactory;
import com.tipuskip.util.AlertHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller untuk Panel Admin (CRUD Skenario dan Indikator - FR-12 & FR-13).
 * Bagian Tanggung Jawab Anggota 2.
 */
public class AdminController {

    // Skenario Controls
    @FXML private TableView<Skenario> tableSkenario;
    @FXML private TableColumn<Skenario, Integer> colSkId;
    @FXML private TableColumn<Skenario, String> colSkJudul;
    @FXML private TableColumn<Skenario, String> colSkJenis;
    @FXML private TableColumn<Skenario, String> colSkKategori;
    @FXML private TableColumn<Skenario, String> colSkTingkat;
    @FXML private TableColumn<Skenario, Boolean> colSkStatus;

    @FXML private Label lblFormSkenario;
    @FXML private TextField txtSkJudul;
    @FXML private TextArea txtSkDeskripsi;
    @FXML private ComboBox<JenisSkenario> cbSkJenis;
    @FXML private TextField txtSkKategori;
    @FXML private ComboBox<TingkatKesulitan> cbSkTingkat;

    private Skenario selectedSkenario = null;

    // Indikator Controls
    @FXML private TableView<IndikatorPenipuan> tableIndikator;
    @FXML private TableColumn<IndikatorPenipuan, Integer> colIndId;
    @FXML private TableColumn<IndikatorPenipuan, String> colIndNama;
    @FXML private TableColumn<IndikatorPenipuan, String> colIndPola;
    @FXML private TableColumn<IndikatorPenipuan, String> colIndTipe;
    @FXML private TableColumn<IndikatorPenipuan, Integer> colIndBobot;
    @FXML private TableColumn<IndikatorPenipuan, Boolean> colIndStatus;

    @FXML private Label lblFormIndikator;
    @FXML private TextField txtIndNama;
    @FXML private TextField txtIndPola;
    @FXML private ComboBox<TipePencocokan> cbIndTipe;
    @FXML private Spinner<Integer> spnIndBobot;
    @FXML private TextArea txtIndDeskripsi;
    @FXML private TextArea txtIndSaran;

    private IndikatorPenipuan selectedIndikator = null;

    private IAdminService adminService;

    @FXML
    public void initialize() {
        adminService = ServiceFactory.getAdminService();

        // Setup dropdowns
        cbSkJenis.setItems(FXCollections.observableArrayList(JenisSkenario.values()));
        cbSkTingkat.setItems(FXCollections.observableArrayList(TingkatKesulitan.values()));
        cbIndTipe.setItems(FXCollections.observableArrayList(TipePencocokan.values()));
        spnIndBobot.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 25, 5));

        setupTabelSkenario();
        setupTabelIndikator();

        // Listener untuk seleksi tabel skenario
        tableSkenario.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSkenario = newVal;
                txtSkJudul.setText(newVal.getJudul());
                txtSkDeskripsi.setText(newVal.getDeskripsi());
                cbSkJenis.setValue(newVal.getJenis());
                txtSkKategori.setText(newVal.getKategori());
                cbSkTingkat.setValue(newVal.getTingkatKesulitan());
                if (lblFormSkenario != null) {
                    lblFormSkenario.setText("Edit Skenario [ID: " + newVal.getId() + "]");
                }
            }
        });

        // Listener untuk seleksi tabel indikator
        tableIndikator.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedIndikator = newVal;
                txtIndNama.setText(newVal.getNamaIndikator());
                txtIndPola.setText(newVal.getPolaPencocokan());
                cbIndTipe.setValue(newVal.getTipePencocokan());
                spnIndBobot.getValueFactory().setValue(newVal.getBobotRisiko());
                txtIndDeskripsi.setText(newVal.getDeskripsi());
                txtIndSaran.setText(newVal.getSaranVerifikasi());
                if (lblFormIndikator != null) {
                    lblFormIndikator.setText("Edit Indikator [ID: " + newVal.getId() + "]");
                }
            }
        });

        muatDataSkenario();
        muatDataIndikator();
    }

    private void setupTabelSkenario() {
        colSkId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colSkJudul.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJudul()));
        colSkJenis.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getJenis().name()));
        colSkKategori.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKategori()));
        colSkTingkat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTingkatKesulitan().getLabel()));
        colSkStatus.setCellValueFactory(d -> new SimpleBooleanProperty(d.getValue().isStatusAktif()).asObject());
    }

    private void setupTabelIndikator() {
        colIndId.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getId()).asObject());
        colIndNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNamaIndikator()));
        colIndPola.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPolaPencocokan()));
        colIndTipe.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipePencocokan().name()));
        colIndBobot.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().getBobotRisiko()).asObject());
        colIndStatus.setCellValueFactory(d -> new SimpleBooleanProperty(d.getValue().isStatusAktif()).asObject());
    }

    @FXML
    public void muatDataSkenario() {
        List<Skenario> list = adminService.getAllSkenario();
        tableSkenario.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void muatDataIndikator() {
        List<IndikatorPenipuan> list = adminService.getAllIndikator();
        tableIndikator.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void handleTambahSkenario() {
        if (txtSkJudul.getText().isBlank() || cbSkJenis.getValue() == null || cbSkTingkat.getValue() == null) {
            AlertHelper.showWarning("Validasi", "Judul, jenis, dan tingkat kesulitan wajib diisi.");
            return;
        }

        Skenario s = new Skenario();
        s.setJudul(txtSkJudul.getText().trim());
        s.setDeskripsi(txtSkDeskripsi.getText().trim());
        s.setJenis(cbSkJenis.getValue());
        s.setKategori(txtSkKategori.getText().trim().isEmpty() ? "Umum" : txtSkKategori.getText().trim());
        s.setTingkatKesulitan(cbSkTingkat.getValue());
        s.setStatusAktif(true);

        boolean success = adminService.tambahSkenario(s);
        if (success) {
            AlertHelper.showInfo("Sukses", "Skenario berhasil ditambahkan.");
            handleResetFormSkenario();
            muatDataSkenario();
        } else {
            AlertHelper.showError("Gagal", "Gagal menyimpan skenario ke basis data.");
        }
    }

    @FXML
    public void handleUbahSkenario() {
        if (selectedSkenario == null) {
            AlertHelper.showWarning("Peringatan", "Pilih skenario yang ingin diubah dari tabel terlebih dahulu.");
            return;
        }

        if (txtSkJudul.getText().isBlank() || cbSkJenis.getValue() == null || cbSkTingkat.getValue() == null) {
            AlertHelper.showWarning("Validasi", "Judul, jenis, dan tingkat kesulitan tidak boleh kosong.");
            return;
        }

        selectedSkenario.setJudul(txtSkJudul.getText().trim());
        selectedSkenario.setDeskripsi(txtSkDeskripsi.getText().trim());
        selectedSkenario.setJenis(cbSkJenis.getValue());
        selectedSkenario.setKategori(txtSkKategori.getText().trim().isEmpty() ? "Umum" : txtSkKategori.getText().trim());
        selectedSkenario.setTingkatKesulitan(cbSkTingkat.getValue());

        boolean success = adminService.ubahSkenario(selectedSkenario);
        if (success) {
            AlertHelper.showInfo("Sukses", "Skenario berhasil diperbarui.");
            handleResetFormSkenario();
            muatDataSkenario();
        } else {
            AlertHelper.showError("Gagal", "Gagal memperbarui skenario.");
        }
    }

    @FXML
    public void handleHapusSkenario() {
        Skenario selected = tableSkenario.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Pilih skenario yang ingin dihapus.");
            return;
        }

        if (AlertHelper.showConfirmation("Konfirmasi Hapus", "Yakin ingin menghapus skenario '" + selected.getJudul() + "'?")) {
            adminService.hapusSkenario(selected.getId());
            handleResetFormSkenario();
            muatDataSkenario();
        }
    }

    @FXML
    public void handleToggleStatusSkenario() {
        Skenario selected = tableSkenario.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Pilih skenario dari tabel untuk mengubah status aktif.");
            return;
        }

        boolean newStatus = !selected.isStatusAktif();
        boolean success = adminService.ubahStatusAktifSkenario(selected.getId(), newStatus);
        if (success) {
            AlertHelper.showInfo("Sukses", "Status skenario berhasil diubah menjadi: " + (newStatus ? "AKTIF" : "NONAKTIF"));
            muatDataSkenario();
        } else {
            AlertHelper.showError("Gagal", "Gagal mengubah status aktif skenario.");
        }
    }

    @FXML
    public void handleResetFormSkenario() {
        selectedSkenario = null;
        tableSkenario.getSelectionModel().clearSelection();
        txtSkJudul.clear();
        txtSkDeskripsi.clear();
        cbSkJenis.setValue(null);
        txtSkKategori.clear();
        cbSkTingkat.setValue(null);
        if (lblFormSkenario != null) {
            lblFormSkenario.setText("Kelola Skenario (Tambah / Ubah)");
        }
    }

    @FXML
    public void handleTambahIndikator() {
        if (txtIndNama.getText().isBlank() || txtIndPola.getText().isBlank() || cbIndTipe.getValue() == null) {
            AlertHelper.showWarning("Validasi", "Nama indikator, pola pencocokan, dan tipe wajib diisi.");
            return;
        }

        IndikatorPenipuan ind = new IndikatorPenipuan();
        ind.setNamaIndikator(txtIndNama.getText().trim());
        ind.setPolaPencocokan(txtIndPola.getText().trim());
        ind.setTipePencocokan(cbIndTipe.getValue());
        ind.setBobotRisiko(spnIndBobot.getValue());
        ind.setDeskripsi(txtIndDeskripsi.getText().trim());
        ind.setSaranVerifikasi(txtIndSaran.getText().trim());
        ind.setStatusAktif(true);

        boolean success = adminService.tambahIndikator(ind);
        if (success) {
            AlertHelper.showInfo("Sukses", "Indikator penipuan berhasil disimpan.");
            handleResetFormIndikator();
            muatDataIndikator();
        } else {
            AlertHelper.showError("Gagal", "Gagal menyimpan indikator ke basis data.");
        }
    }

    @FXML
    public void handleUbahIndikator() {
        if (selectedIndikator == null) {
            AlertHelper.showWarning("Peringatan", "Pilih indikator yang ingin diubah dari tabel terlebih dahulu.");
            return;
        }

        if (txtIndNama.getText().isBlank() || txtIndPola.getText().isBlank() || cbIndTipe.getValue() == null) {
            AlertHelper.showWarning("Validasi", "Nama indikator, pola pencocokan, dan tipe wajib diisi.");
            return;
        }

        selectedIndikator.setNamaIndikator(txtIndNama.getText().trim());
        selectedIndikator.setPolaPencocokan(txtIndPola.getText().trim());
        selectedIndikator.setTipePencocokan(cbIndTipe.getValue());
        selectedIndikator.setBobotRisiko(spnIndBobot.getValue());
        selectedIndikator.setDeskripsi(txtIndDeskripsi.getText().trim());
        selectedIndikator.setSaranVerifikasi(txtIndSaran.getText().trim());

        boolean success = adminService.ubahIndikator(selectedIndikator);
        if (success) {
            AlertHelper.showInfo("Sukses", "Indikator penipuan berhasil diperbarui.");
            handleResetFormIndikator();
            muatDataIndikator();
        } else {
            AlertHelper.showError("Gagal", "Gagal memperbarui indikator.");
        }
    }

    @FXML
    public void handleHapusIndikator() {
        IndikatorPenipuan selected = tableIndikator.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Pilih indikator yang ingin dihapus.");
            return;
        }

        if (AlertHelper.showConfirmation("Konfirmasi Hapus", "Yakin ingin menghapus indikator '" + selected.getNamaIndikator() + "'?")) {
            adminService.hapusIndikator(selected.getId());
            handleResetFormIndikator();
            muatDataIndikator();
        }
    }

    @FXML
    public void handleToggleStatusIndikator() {
        IndikatorPenipuan selected = tableIndikator.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showWarning("Peringatan", "Pilih indikator dari tabel untuk mengubah status aktif.");
            return;
        }

        boolean newStatus = !selected.isStatusAktif();
        boolean success = adminService.ubahStatusAktifIndikator(selected.getId(), newStatus);
        if (success) {
            AlertHelper.showInfo("Sukses", "Status indikator berhasil diubah menjadi: " + (newStatus ? "AKTIF" : "NONAKTIF"));
            muatDataIndikator();
        } else {
            AlertHelper.showError("Gagal", "Gagal mengubah status aktif indikator.");
        }
    }

    @FXML
    public void handleResetFormIndikator() {
        selectedIndikator = null;
        tableIndikator.getSelectionModel().clearSelection();
        txtIndNama.clear();
        txtIndPola.clear();
        cbIndTipe.setValue(null);
        spnIndBobot.getValueFactory().setValue(25);
        txtIndDeskripsi.clear();
        txtIndSaran.clear();
        if (lblFormIndikator != null) {
            lblFormIndikator.setText("Kelola Indikator (Tambah / Ubah)");
        }
    }
}
