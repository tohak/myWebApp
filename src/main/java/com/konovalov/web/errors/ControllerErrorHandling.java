package com.konovalov.web.errors;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

//обработка ошибок и вывод во вью
public class ControllerErrorHandling {
    private ControllerErrorHandling(){
        throw  new IllegalStateException("Utility class");
    }

   public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}