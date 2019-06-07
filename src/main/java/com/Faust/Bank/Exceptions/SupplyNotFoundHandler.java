package com.Faust.Bank.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.notFound;

@RestControllerAdvice
@Slf4j
public class SupplyNotFoundHandler {
    @ExceptionHandler(value = {SupplyNotFoundException.class})
    public ResponseEntity issueNotFound(SupplyNotFoundException ex, WebRequest request) {
        return notFound().build();
    }

}
