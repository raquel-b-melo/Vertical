package com.rmelo.vertical.infrastructure.rest.api;

import com.rmelo.vertical.application.service.DataProcessingService;
import com.rmelo.vertical.shared.utils.FileCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private DataProcessingService dataProcessingService;

    @Mock
    private FileCache fileCache;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        fileController = new FileController(dataProcessingService, fileCache);
    }

    @Test
    void shouldUploadFileSuccessfully() throws IOException, NoSuchAlgorithmException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes());

        when(fileCache.isFileUploaded(any(byte[].class))).thenReturn(false);

        ResponseEntity<String> response = fileController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Arquivo 'test.txt' enviado e gravado com sucesso!"));
        verify(dataProcessingService, times(1)).processAndSaveFile(file);
        verify(fileCache, times(1)).markFileAsUploaded(file.getBytes());
    }

    @Test
    void shouldReturnBadRequestWhenFileIsEmpty() throws IOException, NoSuchAlgorithmException {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[0]);

        ResponseEntity<String> response = fileController.uploadFile(emptyFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Por favor, selecione um arquivo para enviar."));
        verifyNoInteractions(dataProcessingService);
        verifyNoInteractions(fileCache);
    }

    @Test
    void shouldReturnConflictWhenFileAlreadyUploaded() throws IOException, NoSuchAlgorithmException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes());

        when(fileCache.isFileUploaded(any(byte[].class))).thenReturn(true);

        ResponseEntity<String> response = fileController.uploadFile(file);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("O arquivo já foi enviado anteriormente."));
        verifyNoInteractions(dataProcessingService);
        verify(fileCache, times(1)).isFileUploaded(file.getBytes());
    }
}