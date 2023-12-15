package com.safroalex.AutoServiceERP.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        // Создаем структуру для ответа
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("timestamp", new Date());

        // Возвращаем ответ с соответствующим статусом
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Обработчик для CarAlreadyExistsException

    @ExceptionHandler(CarAlreadyExistsException.class)
    public ResponseEntity<Object> handleCarAlreadyExistsException(CarAlreadyExistsException e) {
        // Создаем структуру для ответа
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("timestamp", new Date());

        // Возвращаем ответ с соответствующим статусом
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
