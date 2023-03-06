package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.AuthEntity;
import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.service.AuthService;
import by.piskunou.solvdlaba.web.dto.AuthEntityDTO;
import by.piskunou.solvdlaba.web.dto.PasswordDTO;
import by.piskunou.solvdlaba.web.dto.UserDTO;
import by.piskunou.solvdlaba.web.mapper.AuthMapper;
import by.piskunou.solvdlaba.web.mapper.PasswordMapper;
import by.piskunou.solvdlaba.web.mapper.UserMapper;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final PasswordMapper passwordMapper;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Sign up new user")
    @Parameter(name = "dto", description = "Signed-up user")
    public AuthEntityDTO signUp(@RequestBody @Validated(UserDTO.onSignUp.class) UserDTO dto) {
        User user = userMapper.toEntity(dto);
        return authMapper.toDTO( authService.signUp(user) );
    }

    @PostMapping("/login")
    @Operation(summary = "Login existing user")
    @Parameter(name = "dto", description = "Existing user")
    public AuthEntityDTO login(@RequestBody @Validated(UserDTO.onLogin.class) UserDTO dto) {
        User user = userMapper.toEntity(dto);
        return authMapper.toDTO( authService.login(user) );
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh jwt access token by valid refresh jwt token")
    @Parameter(name = "authEntityDTO", description = "Entity with access and refresh jwt token")
    public AuthEntityDTO refresh(@RequestBody @Validated AuthEntityDTO authEntityDTO) {
        AuthEntity authEntity = authMapper.toEntity(authEntityDTO);
        return authMapper.toDTO( authService.refresh(authEntity) );
    }

    @GetMapping("/password/new")
    @Operation(summary = "Forgot password: put in an email and get link to create new password")
    @Parameter(name = "email", description = "Email where to send a change link")
    public void newPassword(@RequestParam("user_email") @Email(message = "Must be a well-formed email address") String email)
            throws TemplateException, MessagingException, IOException {
        authService.createPassword(email);
    }

    @PostMapping("/password/edit")
    @Operation(summary = "Set up new password")
    @Parameter(name = "token", description = "one-time token to set new password")
    public void editPassword(@RequestParam("reset_password_token") String token, @RequestBody @Validated(PasswordDTO.onEdit.class) PasswordDTO dto) {
        authService.editPassword(token, passwordMapper.toEntity(dto));
    }

}
