package com.jorge.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlRootElement(name = "error")
@Data
public class ApiException {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime zonedDateTime;

    public ApiException(String message, HttpStatus httpStatus, ZonedDateTime zonedDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }
}
