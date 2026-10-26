# Panduan Setup Basis Data MySQL TipuSkip

Basis data TipuSkip telah dirancang menggunakan MySQL 8.0 dengan 8 entitas relasional sesuai PRD.

## Opsi 1: Menggunakan Docker (Sangat Direkomendasikan untuk Tim)

Dengan Docker, semua anggota tim dijamin memiliki konfigurasi database yang identik tanpa perlu menginstal atau mengkonfigurasi MySQL secara manual di laptop masing-masing.

### 1. Jalankan Container
Buka terminal di root direktori project, lalu jalankan:
```bash
docker compose up -d
```

Docker akan secara otomatis:
- Menjalankan container MySQL pada port `3306`.
- Menjalankan container phpMyAdmin pada port `8085`.
- Mengeksekusi script `database/init.sql` (membuat tabel DDL dan mengisi seed data skenario & indikator penipuan).

### 2. Kredensial Database
- **Host**: `localhost`
- **Port**: `3306`
- **Database**: `tipuskip_db`
- **User**: `tipuskip_user`
- **Password**: `tipuskip_password`
- **Root Password**: `root_password`

### 3. Web UI Database (phpMyAdmin)
Buka browser dan akses:
```
http://localhost:8085
```
- Server: `tipuskip-mysql`
- Username: `tipuskip_user` (atau `root`)
- Password: `tipuskip_password` (atau `root_password`)

### 4. Menghentikan Container
```bash
docker compose down
```

---

## Opsi 2: Manual (XAMPP / MySQL Workbench)

Jika anggota tim tidak menggunakan Docker:
1. Pastikan service MySQL aktif di port `3306`.
2. Buat database baru bernama `tipuskip_db`.
3. Buka tab SQL di phpMyAdmin/Workbench, lalu jalankan seluruh isi berkas `database/init.sql`.
4. Sesuaikan konfigurasi username dan password MySQL Anda pada berkas `src/main/resources/application.properties`.
