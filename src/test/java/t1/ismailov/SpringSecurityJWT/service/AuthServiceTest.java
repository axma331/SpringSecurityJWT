package t1.ismailov.SpringSecurityJWT.service;

import t1.ismailov.SpringSecurityJWT.dto.AuthRequest;
import t1.ismailov.SpringSecurityJWT.dto.AuthResponse;

import t1.ismailov.SpringSecurityJWT.dto.RegistrationUserDto;
import t1.ismailov.SpringSecurityJWT.exception.AppError;
import t1.ismailov.SpringSecurityJWT.model.RoleType;
import t1.ismailov.SpringSecurityJWT.model.User;
import t1.ismailov.SpringSecurityJWT.security.jwt.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @Test
    void createAuthToken() {
        AuthRequest authRequest = new AuthRequest("username", "password");
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword("password");
        user.setRoles(List.of(RoleType.USER));

        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        when(authenticationManager.authenticate(authentication)).thenAnswer(invocation -> {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList()));
            return new TestingAuthenticationToken(userDetails, null);
        });
        when(jwtTokenUtils.generateToken(user.getUsername())).thenReturn("token");

        ResponseEntity<?> result = authService.createAuthToken(authRequest);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals("token", ((AuthResponse) result.getBody()).getToken());
    }


    @Test
    public void testCreateAuthTokenFailure() {
        AuthRequest authRequest = new AuthRequest("username", "password");

        Mockito.when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        ResponseEntity<?> result = authService.createAuthToken(authRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Неправильный логин или пароль", ((AppError) result.getBody()).getMessage());
    }


    @Test
    void testCreateUserWithValidPassword() {
        RegistrationUserDto regUserDto = new RegistrationUserDto("username",
                "email", "password", "password");

        ResponseEntity<?> result = authService.createUser(regUserDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void testCreateUserWithInvalidPassword() {
        RegistrationUserDto regUserDto = new RegistrationUserDto("username", "email", "password", "password1");

        ResponseEntity<?> result = authService.createUser(regUserDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Пароли не совпадают", ((AppError) result.getBody()).getMessage());
    }
}