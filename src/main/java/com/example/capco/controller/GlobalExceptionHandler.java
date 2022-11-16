package com.example.capco.controller;

import com.example.capco.dto.ExceptionDto;
import com.example.capco.exception.FeatureEnabledException;
import com.example.capco.exception.FeatureNotFoundException;
import com.example.capco.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<ExceptionDto> featureNotFoundException(FeatureNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFoundException(UserNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeatureEnabledException.class)
    public ResponseEntity<ExceptionDto> futureEnabledException(FeatureEnabledException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.CONFLICT);
    }
}
