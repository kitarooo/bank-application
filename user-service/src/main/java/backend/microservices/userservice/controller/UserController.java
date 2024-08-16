package backend.microservices.userservice.controller;

import backend.microservices.userservice.dto.request.LoginRequest;
import backend.microservices.userservice.dto.request.RegistrationRequest;
import backend.microservices.userservice.dto.request.UserRequest;
import backend.microservices.userservice.dto.response.AuthenticationResponse;
import backend.microservices.userservice.dto.response.UserResponse;
import backend.microservices.userservice.entity.User;
import backend.microservices.userservice.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("/updateProfile/{id}")
    public UserResponse updateProfile(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUserProfileById(id, request);
    }
}
