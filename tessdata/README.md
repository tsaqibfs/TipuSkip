# Folder Tessdata (Tesseract OCR Language Models)

Folder ini digunakan oleh Tess4J untuk mengekstraksi teks dari gambar tangkapan layar.

## Cara Mengisi File Traineddata:
1. Unduh model bahasa dari repositori resmi Tesseract OCR:
   - Bahasa Indonesia (`ind.traineddata`): https://github.com/tesseract-ocr/tessdata_fast/raw/main/ind.traineddata
   - Bahasa Inggris (`eng.traineddata`): https://github.com/tesseract-ocr/tessdata_fast/raw/main/eng.traineddata
2. Simpan file `.traineddata` di dalam folder ini (`tessdata/`).
3. File `.traineddata` telah dimasukkan ke dalam `.gitignore` agar tidak membebani repository GitHub (karena ukuran filenya relatif besar).

## Fallback Otomatis:
Jika file `.traineddata` belum diunduh, sistem `TextExtractor` di TipuSkip memiliki fitur **Mock/Simulator Fallback** sehingga anggota tim lain (UI & Database) tetap dapat menguji fitur unggah tangkapan layar tanpa mengalami crash!
