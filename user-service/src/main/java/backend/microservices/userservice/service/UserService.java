package backend.microservices.userservice.service;

import backend.microservices.userservice.dto.request.LoginRequest;
import backend.microservices.userservice.dto.request.RegistrationRequest;
import backend.microservices.userservice.dto.request.UserRequest;
import backend.microservices.userservice.dto.response.AuthenticationResponse;
import backend.microservices.userservice.dto.response.UserResponse;
import backend.microservices.userservice.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {
    UserResponse updateUserProfileById(Long id, UserRequest request);
    User getUserByUsername(String username);

}
