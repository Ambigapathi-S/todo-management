package com.example.todomanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Setter
@AllArgsConstructor
public class ToDoAPIException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
