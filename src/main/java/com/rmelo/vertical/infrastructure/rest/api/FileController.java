package com.rmelo.vertical.infrastructure.rest.api;

import com.rmelo.vertical.application.service.DataProcessingService;
import com.rmelo.vertical.shared.utils.FileCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final DataProcessingService dataProcessingService;
    private final FileCache fileCache;

    public FileController(DataProcessingService dataProcessingService, FileCache fileCache) {
        this.dataProcessingService = dataProcessingService;
        this.fileCache = fileCache;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecione um arquivo para enviar.");
        }

        byte[] fileBytes = file.getBytes();
        if (fileCache.isFileUploaded(fileBytes)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("O arquivo já foi enviado anteriormente.");
        }

        dataProcessingService.processAndSaveFile(file);
        fileCache.markFileAsUploaded(fileBytes);
        return ResponseEntity.ok("Arquivo '" + file.getOriginalFilename() + "' enviado e gravado com sucesso!");
    }
}