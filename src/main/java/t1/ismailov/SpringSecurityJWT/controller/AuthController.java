package t1.ismailov.SpringSecurityJWT.controller;

import t1.ismailov.SpringSecurityJWT.dto.AuthRequest;
import t1.ismailov.SpringSecurityJWT.dto.AuthResponse;
import t1.ismailov.SpringSecurityJWT.dto.RegistrationUserDto;
import t1.ismailov.SpringSecurityJWT.exception.AppError;
import t1.ismailov.SpringSecurityJWT.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Аутентификация и регистрация", description = "Методы генерации токена и регистрации пользователя")

public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Создание токена",
            description = "Метод создания токена аутентификации на основании логина и пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен для аутентификации", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Неправильный логин или пароль",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppError.class)))
    })
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authRequest){
        return authService.createAuthToken(authRequest);
    }
    @Operation(summary = "Создание пользователя",
            description = "Метод создания нового пользователя на основании регистрационный данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь %s создан",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
            @ApiResponse(responseCode = "400", description = "Пароли не совпадают", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AppError.class)),
            })
    })
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody RegistrationUserDto regUserDto){
       return authService.createUser(regUserDto);
    }


}
