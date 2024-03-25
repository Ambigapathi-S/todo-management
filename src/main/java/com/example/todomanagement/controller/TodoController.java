package com.example.todomanagement.controller;

import com.example.todomanagement.dto.LoginRequestDto;
import com.example.todomanagement.dto.TodoDto;
import com.example.todomanagement.security.JwtUtil;
import com.example.todomanagement.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("api/todos")
public class TodoController {
    private TodoService todoService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                requestDto.getUsername(),
                requestDto.getPassword()
        );
        authenticationManager.authenticate(token);
        String jwt = jwtUtil.generate(requestDto.getUsername());
        return ResponseEntity.ok(jwt);
    }

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
    @PutMapping("{id}")
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable("id") Long id) {
        TodoDto updatedTodo = todoService.updateTodo(todoDto, id);
        return ResponseEntity.ok(updatedTodo);
    }
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok("Task deleted successfully!");
    }
    @PatchMapping("{id}/complete")
    public ResponseEntity<TodoDto> completeTodo(@PathVariable("id") Long todoId){
        TodoDto updatedTodo = todoService.completeTodo(todoId);
        return ResponseEntity.ok(updatedTodo);
    }

    // Build In Complete Todo REST API
    @PatchMapping("{id}/in-complete")
    public ResponseEntity<TodoDto> inCompleteTodo(@PathVariable("id") Long todoId){
        TodoDto updatedTodo = todoService.inCompleteTodo(todoId);
        return ResponseEntity.ok(updatedTodo);
    }
}
