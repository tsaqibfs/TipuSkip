package com.tipuskip.service;

import com.tipuskip.config.DatabaseConnection;
import com.tipuskip.service.impl.AdminServiceImpl;
import com.tipuskip.service.impl.AnalisisServiceImpl;
import com.tipuskip.service.impl.MockSimulasiService;
import com.tipuskip.service.impl.SimulasiServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Factory (Factory Design Pattern).
 * Menyediakan instance service secara terpusat untuk UI Controller.
 * Memisahkan Controller dari detail implementasi Service/DAO.
 */
public class ServiceFactory {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFactory.class);

    private static ISimulasiService simulasiService;
    private static IAnalisisService analisisService;
    private static IAdminService adminService;

    public static synchronized ISimulasiService getSimulasiService() {
        if (simulasiService == null) {
            if (DatabaseConnection.getInstance().testConnection()) {
                logger.info("Menggunakan SimulasiServiceImpl (Database Terhubung).");
                simulasiService = new SimulasiServiceImpl();
            } else {
                logger.warn("Koneksi Database tidak terdeteksi. Beralih ke MockSimulasiService.");
                simulasiService = new MockSimulasiService();
            }
        }
        return simulasiService;
    }

    public static synchronized IAnalisisService getAnalisisService() {
        if (analisisService == null) {
            analisisService = new AnalisisServiceImpl();
        }
        return analisisService;
    }

    public static synchronized IAdminService getAdminService() {
        if (adminService == null) {
            adminService = new AdminServiceImpl();
        }
        return adminService;
    }

    public static void setSimulasiService(ISimulasiService service) {
        simulasiService = service;
    }

    public static void setAnalisisService(IAnalisisService service) {
        analisisService = service;
    }

    public static void setAdminService(IAdminService service) {
        adminService = service;
    }
}
