package com.example.todomanagement.service;

import com.example.todomanagement.dto.TodoDto;

import java.util.List;

public interface TodoService {
    TodoDto addTodo(TodoDto todoDto);
    List<TodoDto> getAllTodos();
    TodoDto getTodo(Long id);
    TodoDto updateTodo(TodoDto todoDto, Long id);
    void deleteTodo(Long id);
}
