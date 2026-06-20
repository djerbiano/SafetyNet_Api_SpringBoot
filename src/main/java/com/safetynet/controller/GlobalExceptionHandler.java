package com.safetynet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Intercepte les appels vers une URL qui ne correspond à aucun endpoint
     *
     * @param ex l'exception levée par spring quand aucune route ne correspond
     * @return un message JSON avec le statu HTTP 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex) {
        logger.warn("Endpoint inexistant appelé : {}", ex.getMessage());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 404);
        response.put("error", "Endpoint not found");
        response.put("message", "Endpoint does not exist. Check the URL and HTTP method");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
