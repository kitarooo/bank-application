package backend.microservices.userservice.service.impl;

import backend.microservices.userservice.dto.request.LoginRequest;
import backend.microservices.userservice.dto.request.RegistrationRequest;
import backend.microservices.userservice.dto.response.AuthenticationResponse;
import backend.microservices.userservice.entity.User;
import backend.microservices.userservice.entity.enums.Role;
import backend.microservices.userservice.exception.IncorrectDataException;
import backend.microservices.userservice.exception.UserAlreadyExistException;
import backend.microservices.userservice.repository.UserRepository;
import backend.microservices.userservice.security.jwt.JwtService;
import backend.microservices.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String registration(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Пользователь с username: " + request.getUsername() + " уже существует!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        return "Регистрация прошла успешно!";
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IncorrectDataException("Данные введены неправильно!"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword()) && user.getUsername().equals(request.getUsername())) {
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
        } else {
            throw new IncorrectDataException("Данные введены неправильно!");
        }
    }
}
