package backend.microservices.userservice.service;

import backend.microservices.userservice.dto.request.LoginRequest;
import backend.microservices.userservice.dto.request.RegistrationRequest;
import backend.microservices.userservice.dto.response.AuthenticationResponse;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AuthService {
    String registration(RegistrationRequest request);
    AuthenticationResponse login(LoginRequest request);
}
