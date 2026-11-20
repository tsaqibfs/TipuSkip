@echo off
title TipuSkip - Edukasi Pencegahan Penipuan Digital
cd /d "%~dp0"
echo ========================================================
echo   Memulai Aplikasi Desktop TipuSkip (JavaFX PBO)
echo   UI/UX & Simulasi Interaktif - Anggota 1
echo ========================================================
echo.

:: Auto-detect JAVA_HOME if not defined
if "%JAVA_HOME%"=="" (
    for /d %%i in ("%ProgramFiles%\Java\jdk*") do (
        set "JAVA_HOME=%%i"
    )
)

if not "%JAVA_HOME%"=="" (
    echo [OK] Menggunakan JAVA_HOME: %JAVA_HOME%
)

echo.
echo Sedang memuat antarmuka grafis desktop...
call mvnw.cmd javafx:run
if %errorlevel% neq 0 (
    echo.
    echo Terjadi kendala saat menjalankan aplikasi.
    pause
)
