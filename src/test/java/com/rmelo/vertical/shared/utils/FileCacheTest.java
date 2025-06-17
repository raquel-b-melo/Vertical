package com.rmelo.vertical.shared.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class FileCacheTest {

    @InjectMocks
    private FileCache fileCache;

    @BeforeEach
    void setUp() {
        fileCache = new FileCache();
    }

    @Test
    void shouldReturnFalseWhenFileNotUploaded() throws NoSuchAlgorithmException {
        byte[] fileBytes = "test file".getBytes();
        assertFalse(fileCache.isFileUploaded(fileBytes));
    }

    @Test
    void shouldReturnTrueWhenFileIsUploaded() throws NoSuchAlgorithmException {
        byte[] fileBytes = "test file".getBytes();

        fileCache.markFileAsUploaded(fileBytes);
        assertTrue(fileCache.isFileUploaded(fileBytes));
    }

    @Test
    void shouldGenerateSameHashForSameFile() throws NoSuchAlgorithmException {
        byte[] fileBytes1 = "test file".getBytes();
        byte[] fileBytes2 = "test file".getBytes();

        String hash1 = fileCache.generateFileHash(fileBytes1);
        String hash2 = fileCache.generateFileHash(fileBytes2);

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldGenerateDifferentHashForDifferentFiles() throws NoSuchAlgorithmException {
        byte[] fileBytes1 = "test file 1".getBytes();
        byte[] fileBytes2 = "test file 2".getBytes();

        String hash1 = fileCache.generateFileHash(fileBytes1);
        String hash2 = fileCache.generateFileHash(fileBytes2);

        assertNotEquals(hash1, hash2);
    }
}