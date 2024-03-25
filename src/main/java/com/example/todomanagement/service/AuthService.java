package com.example.todomanagement.service;

import com.example.todomanagement.dto.JwtAuthResponse;
import com.example.todomanagement.dto.LoginRequestDto;
import com.example.todomanagement.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);

    JwtAuthResponse login(LoginRequestDto loginRequestDto);
}
