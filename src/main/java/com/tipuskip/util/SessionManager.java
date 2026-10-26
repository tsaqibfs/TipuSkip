package com.tipuskip.util;

import com.tipuskip.model.Pengguna;
import com.tipuskip.model.Role;

/**
 * Menyimpan status sesi pengguna yang sedang aktif di aplikasi desktop.
 */
public class SessionManager {
    private static Pengguna currentUser;

    static {
        // Default session untuk kemudahan pengujian awal
        currentUser = new Pengguna(2, "budi", "budi@example.com", "", Role.PENGGUNA);
    }

    public static Pengguna getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Pengguna user) {
        currentUser = user;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
}
