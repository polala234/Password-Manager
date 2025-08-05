# Password Manager (Java Swing)

A simple password manager with a graphical interface built using Java Swing. It allows users to securely store, encrypt, and manage passwords organized into categories.

## Features

- Add, edit, and delete password entries
- Organize passwords into categories
- Built-in strong password generator
- AES encryption/decryption of password data
- Save and load encrypted data to/from a file
- Custom icons and intuitive Swing-based UI

## Project Structure

- `Main.java` – application entry point
- `MainFrame.java` – main GUI window
- `MainController.java` – handles UI logic and data flow
- `Password.java` – password data model
- `PasswordTableModel.java` – table model for displaying data in GUI
- `PasswordDatabase.java` – in-memory storage and file I/O
- `FileManager.java` – file handling logic
- `PasswordCipher.java` – AES encryption/decryption logic
- `PasswordEntryDialog.java` – dialog for adding/editing passwords
- `PasswordGeneratorDialog.java` – dialog for generating secure passwords
- `CategoryManager.java` – category handling logic
- `MyIcons.java` – icon resources

# Password Manager PL

Prosty menedżer haseł z GUI oparty na Java Swing. Umożliwia przechowywanie, szyfrowanie i zarządzanie hasłami w kategoriach.

## Funkcje

- Dodawanie, edytowanie i usuwanie wpisów haseł
- Kategoryzacja haseł
- Generator silnych haseł
- Szyfrowanie i deszyfrowanie danych za pomocą AES
- Zapis danych do pliku i ich odczyt
- Ikony i GUI stworzone z wykorzystaniem Swing

## Struktura projektu

- `Main.java` – punkt wejściowy aplikacji
- `MainFrame.java` – główne okno GUI
- `MainController.java` – logika sterująca aplikacją
- `Password.java` – model danych hasła
- `PasswordTableModel.java` – model tabeli GUI
- `PasswordDatabase.java` – baza danych haseł w pamięci + zapis/odczyt
- `FileManager.java` – obsługa plików
- `PasswordCipher.java` – szyfrowanie/deszyfrowanie AES
- `PasswordEntryDialog.java` – okno dialogowe do dodawania/edycji hasła
- `PasswordGeneratorDialog.java` – generator haseł
- `CategoryManager.java` – obsługa kategorii
- `MyIcons.java` – ikony aplikacji

