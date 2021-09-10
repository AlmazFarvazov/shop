package ru.itis.afarvazov.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.itis.afarvazov.dto.ApiExceptionDto;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiExceptionDto> handleException(Exception exception) {
        ApiExceptionDto dto = ApiExceptionDto.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(dto, dto.getStatus());
    }

}
