package com.rmelo.vertical.shared.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileCache {
    private final ConcurrentHashMap<String, Boolean> uploadedFiles = new ConcurrentHashMap<>();

    public boolean isFileUploaded(byte[] fileBytes) throws NoSuchAlgorithmException {
        String hash = generateFileHash(fileBytes);
        return uploadedFiles.containsKey(hash);
    }

    public void markFileAsUploaded(byte[] fileBytes) throws NoSuchAlgorithmException {
        String hash = generateFileHash(fileBytes);
        uploadedFiles.put(hash, true);
    }

    String generateFileHash(byte[] fileBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}