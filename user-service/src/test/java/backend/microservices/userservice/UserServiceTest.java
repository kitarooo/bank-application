package backend.microservices.userservice;

import backend.microservices.userservice.dto.request.LoginRequest;
import backend.microservices.userservice.dto.request.RegistrationRequest;
import backend.microservices.userservice.dto.request.UserRequest;
import backend.microservices.userservice.dto.response.AuthenticationResponse;
import backend.microservices.userservice.dto.response.UserResponse;
import backend.microservices.userservice.entity.User;
import backend.microservices.userservice.exception.IncorrectDataException;
import backend.microservices.userservice.exception.NotFoundException;
import backend.microservices.userservice.exception.UserAlreadyExistException;
import backend.microservices.userservice.repository.UserRepository;
import backend.microservices.userservice.security.jwt.JwtService;
import backend.microservices.userservice.service.impl.AuthServiceImpl;
import backend.microservices.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private User user;
    private User updatedUser;
    private UserRequest userRequest;


    @BeforeEach
    public void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("username");
        registrationRequest.setFirstname("firstname");
        registrationRequest.setLastname("lastname");
        registrationRequest.setPassword("password");

        loginRequest = new LoginRequest("username", "password");
        user = User.builder()
                .id(1L)
                .username("username")
                .lastname("lastname")
                .firstname("firstname")
                .phoneNumber("phoneNumber")
                .password("password")
                .build();

        updatedUser = User.builder()
                .id(1L)
                .username("username")
                .lastname("lastname")
                .firstname("firstname")
                .phoneNumber("phoneNumber")
                .password("password")
                .build();
        userRequest = new UserRequest();
        userRequest.setFirstname("updatedFirstname");
        userRequest.setLastname("updatedLastname");
        userRequest.setUsername("upadtedLastname");
        userRequest.setPassword("updatedPassword");
        userRequest.setPhoneNumber("updatedPhoneNumber");

    }


    // REGISTRATION TEST
    @Test
    public void successRegistry() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        String result = authService.registration(registrationRequest);
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals("Регистрация прошла успешно!", result);
    }

    @Test
    public void testRegistrationUserAlreadyExists() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User()));

        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            authService.registration(registrationRequest);
        });

        assertEquals("Пользователь с username: username уже существует!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    // LOGIN TEST
    @Test
    public void successLoginTest() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt");

        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        verify(jwtService, times(1)).generateToken(user);

        assertNotNull(authenticationResponse);
        assertEquals("jwt", authenticationResponse.getAccessToken());
    }

    @Test
    public void incorrectLoginTest() {
        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        IncorrectDataException incorrectDataException = assertThrows(IncorrectDataException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Данные введены неправильно!", incorrectDataException.getMessage());
        verify(jwtService, never()).generateToken(any(User.class));
    }


    // UPDATE USER PROFILE TEST
    @Test
    public void successUpdateUserProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.updateUserProfileById(1L, userRequest);
        verify(userRepository, times(1)).save(user);

        assertNotNull(result);
        assertEquals(userRequest.getUsername(), result.getUsername());
        assertEquals(userRequest.getFirstname(), result.getFirstname());
        assertEquals(userRequest.getLastname(), result.getLastname());
        assertEquals(userRequest.getPhoneNumber(), result.getPhoneNumber());
    }
    @Test
    public void updateUserProfileNotFoundError() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            userService.updateUserProfileById(1L, userRequest);
        });

        assertEquals("Пользователь не найден!", notFoundException.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    // Get user by username
    @Test
    public void getUserByUsername() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByUsername("username");
        assertNotNull(foundUser);
        assertEquals("username", foundUser.getUsername());
        assertEquals("firstname", foundUser.getFirstname());
        assertEquals("lastname", foundUser.getLastname());
        assertEquals("phoneNumber", foundUser.getPhoneNumber());
    }
}
