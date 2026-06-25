package com.safetynet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gère les erreurs communes à tous les controllers
 * de l'application, afin de toujours renvoyer un message JSON clair
 * au lieu de la trace technique par défaut de Spring.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Intercepte les appels vers une URL qui ne correspond à aucun endpoint.
     *
     * @param ex l'exception levée par Spring quand aucune route ne correspond.
     * @return un message JSON avec le statut HTTP 404.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoResourceFoundException ex) {
        logger.warn("Endpoint inexistant appelé : {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Endpoint not found",
                "Endpoint does not exist. Check the URL and HTTP method");
    }

    /**
     * Intercepte les appels où un paramètre obligatoire est absent de la requête.
     *
     * @param ex l'exception levée par Spring quand un @RequestParam requis est manquant.
     * @return un message JSON avec le statut HTTP 400 et le nom du paramètre manquant.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        logger.warn("Paramètre manquant : {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing parameter",
                "Required parameter '" + ex.getParameterName() + "' is missing.");
    }

    /**
     * Intercepte toute exception non gérée
     * par les autres méthodes, pour éviter qu'une erreur inattendue ne
     * remonte sans réponse JSON cohérente.
     *
     * @param ex l'exception inattendue levée pendant le traitement de la requête.
     * @return un message JSON avec le statut HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        logger.error("Erreur inattendue : {}", ex.getMessage(), ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
                message);
    }

    /**
     * Construit une réponse JSON uniforme pour toutes les erreurs gérées
     * par cette classe, afin d'éviter de répéter la même structure trois fois.
     *
     * @param status  le statut HTTP à renvoyer.
     * @param error   le libellé court de l'erreur.
     * @param message le message détaillé destiné au consommateur de l'API.
     * @return la réponse HTTP prête à être renvoyée par Spring.
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}