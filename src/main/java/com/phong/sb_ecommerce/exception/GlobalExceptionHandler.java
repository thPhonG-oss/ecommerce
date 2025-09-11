package com.phong.sb_ecommerce.exception;

import com.phong.sb_ecommerce.payload.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e){
        Map<String, String> errors = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.toString(), message, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        if (defaultMessage == null || defaultMessage.isEmpty()) {
            defaultMessage = "Validation failed for one or more fields. Please check the input data.";
        }
        e.getBindingResult()
                .getAllErrors().forEach(err -> {
                    String field = ((FieldError)err).getField();
                    String message = err.getDefaultMessage();
                    errors.put(field, message);
                });
        return new ResponseEntity<>(
                new ErrorResponse<>(HttpStatus.BAD_REQUEST.value(), defaultMessage, errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {ResourcesNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourcesNotFoundException e){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.toString(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {APIException.class})
    public ResponseEntity<ApiResponse<Object>> handleAPIException(APIException e){
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourcesAlreadyExistException.class})
    public ResponseEntity<ApiResponse<Object>> handleResourcesAlreadyExistException(ResourcesAlreadyExistException e){
        return new ResponseEntity<>(
                new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.toString(),
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e){
        return new ResponseEntity<>(new ApiResponse<>(
            HttpStatus.NOT_FOUND.toString(),
            e.getMessage()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e){
        return new ResponseEntity<>(ApiResponse.builder()
            .status(HttpStatus.FORBIDDEN.toString())
            .message(e.getMessage())
            .build(),
            HttpStatus.FORBIDDEN);
    }
}
