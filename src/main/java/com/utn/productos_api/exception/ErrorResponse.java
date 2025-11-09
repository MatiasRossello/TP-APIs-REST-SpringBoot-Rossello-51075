package com.utn.productos_api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Getter
@Setter

public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer httpStatus;
    private String errorMessage;
    private String route;

}
