package com.epam.estart.controller;

import com.epam.estart.dto.User;
import com.epam.estart.security.JwtDto;
import com.epam.estart.service.AuthService;
import com.epam.estart.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final AuthService authService;

  @GetMapping("/{id}")
  public User getById(@PathVariable UUID id) {
    return userService.getById(id);
  }

  @PostMapping
  public JwtDto create(@RequestBody User user) {
    return authService.register(user);
  }

  @PutMapping
  public User update(@RequestBody User user) {
    return userService.updateAndReturn(user);
  }
}
