package com.tipuskip.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Pengelola Koneksi JDBC ke MySQL (Pola Desain Singleton).
 * Bagian Tanggung Jawab Anggota 2.
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static DatabaseConnection instance;
    private Connection connection;

    private String url;
    private String username;
    private String password;

    private DatabaseConnection() {
        loadProperties();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception e) {
            logger.warn("application.properties tidak ditemukan, menggunakan nilai default.", e);
        }

        String host = props.getProperty("db.host", "localhost");
        String port = props.getProperty("db.port", "3306");
        String name = props.getProperty("db.name", "tipuskip_db");
        this.username = props.getProperty("db.username", "tipuskip_user");
        this.password = props.getProperty("db.password", "tipuskip_password");

        this.url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta&characterEncoding=UTF-8",
                host, port, name
        );
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                logger.info("Koneksi ke MySQL database berhasil dibuat.");
            } catch (ClassNotFoundException e) {
                logger.error("Driver MySQL JDBC tidak ditemukan di classpath!", e);
                throw new SQLException("Driver MySQL JDBC tidak ditemukan.", e);
            } catch (SQLException e) {
                logger.error("Gagal terhubung ke database MySQL di {}: {}", url, e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Koneksi ke MySQL berhasil ditutup.");
            } catch (SQLException e) {
                logger.error("Gagal menutup koneksi database: {}", e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}
