package com.example.todomanagement.service.impl;

import com.example.todomanagement.dto.JwtAuthResponse;
import com.example.todomanagement.dto.LoginRequestDto;
import com.example.todomanagement.dto.RegisterDto;
import com.example.todomanagement.entity.Role;
import com.example.todomanagement.entity.User;
import com.example.todomanagement.exception.ToDoAPIException;
import com.example.todomanagement.repository.RoleRepository;
import com.example.todomanagement.repository.UserRepository;
import com.example.todomanagement.security.JwtUtil;
import com.example.todomanagement.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;


    @Override
    public String register(RegisterDto registerDto) {
        // To check whether a user already exists with the same username
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        // To check whether a user already exists with the same email
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ToDoAPIException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Creating a user from the DTO and to store in the user table
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);
        return "User Registered Successfully!!";
    }

    @Override
    public JwtAuthResponse login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generate(loginRequestDto.getUsername());

        Optional<User> userOptional = userRepository.findByUsername(loginRequestDto.getUsername());

        String role = "";
        if (userOptional.isPresent()) {
            User loggedInUser = userOptional.get();
            Optional<Role> optionalRole = loggedInUser.getRoles().stream().findFirst();

            Role userRole = optionalRole.get();
            role = userRole.getName();
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(jwt);
        jwtAuthResponse.setRole(role);

        return jwtAuthResponse;
    }
}
