package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.AuthEntity;
import by.piskunou.solvdlaba.domain.Password;
import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import by.piskunou.solvdlaba.service.AuthService;
import by.piskunou.solvdlaba.service.EmailService;
import by.piskunou.solvdlaba.service.JwtService;
import by.piskunou.solvdlaba.service.UserService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthEntity signUp(User user) {
        userService.create(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new AuthEntity(accessToken, refreshToken);
    }

    @Override
    public AuthEntity login(User user) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authManager.authenticate(authInputToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return new AuthEntity(accessToken, refreshToken);
    }

    @Override
    public AuthEntity refresh(AuthEntity authEntity) {
        if(!jwtService.isValidRefreshToken(authEntity.getRefreshToken())) {
            throw new AccessDeniedException("Access denied");
        }
        String username = jwtService.extractUsername( authEntity.getRefreshToken() );
        UserDetailsImpl userDetails = new UserDetailsImpl( userService.findByUsername(username) );
        authEntity.setAccessToken( jwtService.generateAccessToken(userDetails)  );
        return authEntity;
    }

    @Override
    public void createPassword(String email) throws TemplateException, MessagingException, IOException {
        User user = userService.findByEmail(email);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String editPasswordToken = jwtService.generateEditPasswordToken(userDetails);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", user);
        templateModel.put("token", editPasswordToken);

        emailService.sendMessage(email, templateModel);
    }

    @Override
    public void editPassword(String token, Password password) {
        if(!jwtService.isValidEditPasswordToken(token)) {
            throw new JWTVerificationException("Invalid token");
        }
        //parse and create
        String username = jwtService.extractUsername(token);
        userService.updatePasswordByUsername(username, password.getNewPassword());
    }

}
