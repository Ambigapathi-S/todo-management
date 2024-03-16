package com.example.todomanagement.controller;

import com.example.todomanagement.dto.TodoDto;
import com.example.todomanagement.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/todos")
public class TodoController {
    private TodoService todoService;
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) {
        TodoDto savedTodo = todoService.addTodo(todoDto);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"USER\")")
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        List<TodoDto> todos = todoService.getAllTodos();
        return ResponseEntity.ok().body(todos);
    }
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"USER\")")
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodo(@PathVariable("id") Long id) {
        TodoDto todoDto = todoService.getTodo(id);
        return ResponseEntity.ok().body(todoDto);
    }
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PutMapping("/update/{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable("id") Long id) {
        TodoDto updatedUser = todoService.getTodo(id);
        updatedUser.setTitle(todoDto.getTitle());
        updatedUser.setDescription(todoDto.getDescription());
        updatedUser.setCompleted(todoDto.isCompleted());
        return new ResponseEntity<>(todoService.addTodo(updatedUser), HttpStatus.OK);
    }
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Task deleted successfully!");
    }
}
