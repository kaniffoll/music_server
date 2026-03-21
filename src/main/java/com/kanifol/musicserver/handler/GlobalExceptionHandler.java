package com.kanifol.musicserver.handler;

import com.kanifol.musicserver.repository.minio.exc.MinioException;
import com.kanifol.musicserver.service.exc.CustomNoSuchElementException;
import com.kanifol.musicserver.service.exc.UploadTrackProcessException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MinioException.class)
    public ResponseEntity<?> handleEmptyMinioException(MinioException e) {
        String message =
                messageSource.getMessage(e.getCode(), null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UploadTrackProcessException.class)
    public ResponseEntity<?> handleUploadTrackProcessException(UploadTrackProcessException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomNoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchMusicElementException(CustomNoSuchElementException e) {
        String message =
                messageSource.getMessage(e.getCode(), null, Locale.getDefault());
        String details = e.getDetails().toString();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message + " " + details);
    }
}
