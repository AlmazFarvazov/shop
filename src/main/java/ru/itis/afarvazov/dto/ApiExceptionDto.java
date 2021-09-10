package ru.itis.afarvazov.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiExceptionDto {
    private HttpStatus status;
    private String message;
}
