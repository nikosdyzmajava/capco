package com.example.capco.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FeatureNotFoundException extends RuntimeException {

    private Long featureId;

    public String getMessage() {
        return "Feature with id: " + featureId + " not found!";
    }
}
