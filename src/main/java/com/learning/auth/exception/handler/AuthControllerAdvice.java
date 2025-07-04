package com.learning.auth.exception.handler;

import com.learning.auth.dto.ErrorResponse;
import com.learning.auth.exception.RoleNotFoundException;
import com.learning.auth.exception.UnAuthException;
import com.learning.auth.exception.UserNotFoundException;
import com.learning.auth.exception.UserNotVerifiedException;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AuthControllerAdvice.class);

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex) {
        LOGGER.error("Role not found: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<?> handleUserNotVerifiedException(UserNotVerifiedException ex) {
        LOGGER.error("User not verified: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getMessage(), 403);
        return ResponseEntity.status(403).body(errorResponse);
    }

    @ExceptionHandler(UnAuthException.class)
    public ResponseEntity<?> handleUnAuthException(UnAuthException ex) {
        LOGGER.error("Invalid token: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse("Username or password is not valid", ex.getMessage(), 401);
        return ResponseEntity.status(401).body(errorResponse);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        LOGGER.error("User not found: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse("User not found", ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.error("Illegal argument: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse("Invalid input", ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        LOGGER.error("An unexpected error occurred: {}", ex);
        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }
}
