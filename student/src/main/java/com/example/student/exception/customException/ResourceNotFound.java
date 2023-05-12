package com.example.student.exception.customException;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    private final static long serialVersionUID = 1L;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;


}
