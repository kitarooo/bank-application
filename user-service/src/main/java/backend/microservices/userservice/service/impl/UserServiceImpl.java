package backend.microservices.userservice.service.impl;

import backend.microservices.userservice.dto.request.UserRequest;
import backend.microservices.userservice.dto.response.UserResponse;
import backend.microservices.userservice.entity.User;
import backend.microservices.userservice.exception.NotFoundException;
import backend.microservices.userservice.repository.UserRepository;
import backend.microservices.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse updateUserProfileById(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
    }
}
