package io.github.mateuszuran.ptdmanagerpersonalized.exception;

import java.io.IOException;

public class FileUploadException extends RuntimeException{
    public FileUploadException() {
        super("File not found, please try again.");
    }

    public FileUploadException(String type) {
        super("Invalid file type: " + type + ". Please try again.");
    }

    public FileUploadException(IOException e) {
        super("Failed to upload: " + e);
    }
}
