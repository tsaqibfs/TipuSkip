package com.tipuskip.util;

import java.io.File;
import java.util.Set;

/**
 * Utilitas validasi berkas tangkapan layar yang diunggah.
 */
public class FileValidator {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpg", "jpeg", "bmp", "webp");
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    public static boolean isValidImage(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        if (file.length() > MAX_FILE_SIZE_BYTES) {
            return false;
        }

        String name = file.getName().toLowerCase();
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1 || lastDot == name.length() - 1) {
            return false;
        }

        String ext = name.substring(lastDot + 1);
        return ALLOWED_EXTENSIONS.contains(ext);
    }
}
