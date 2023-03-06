package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.AuthEntity;
import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import by.piskunou.solvdlaba.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void verifySignUpTest() {
        User user = User.builder()
                        .username("Cool boy333")
                        .password("MTIzNA==")
                        .build();

        assertNotNull(authService.signUp(user));
        verify(userService).create(user);
        verify(jwtService).generateAccessToken(any());
        verify(jwtService).generateRefreshToken(any());
    }

    @Test
    void verifyLoginTest() {
        User user = User.builder()
                        .username("Cool boy333")
                        .password("MTIzNA==")
                        .build();
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken("Cool boy333", "MTIzNA==");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Collections.singletonList("ADMIN");
        UsernamePasswordAuthenticationToken authReturnToken = new UsernamePasswordAuthenticationToken(userDetails,
                null,
                Collections.singletonList( new SimpleGrantedAuthority( "ADMIN")));
        when(authManager.authenticate(authInputToken)).thenReturn(authReturnToken);

        assertNotNull(authService.login(user));
        verify(authManager).authenticate(authInputToken);
        verify(jwtService).generateAccessToken(userDetails);
        verify(jwtService).generateRefreshToken(userDetails);
    }

    @Test
    void verifyRefreshToken() {
        AuthEntity authEntity = AuthEntity.builder()
                                          .refreshToken("validRefreshToken")
                                          .build();
        when(jwtService.isValidRefreshToken("validRefreshToken")).thenReturn(true);
        when(jwtService.extractUsername("validRefreshToken")).thenReturn("Kate");

        assertNotNull(authService.refresh(authEntity));
        verify(jwtService).isValidRefreshToken("validRefreshToken");
        verify(jwtService).extractUsername("validRefreshToken");
        verify(userService).findByUsername("Kate");
        verify(jwtService).generateAccessToken(any());
    }

    @Test
    void verifyInvalidRefreshToken() {
        AuthEntity authEntity = AuthEntity.builder()
                                          .refreshToken("invalidRefreshToken")
                                          .build();
        assertThrows(AccessDeniedException.class, () -> authService.refresh(authEntity));
        verify(jwtService).isValidRefreshToken("invalidRefreshToken");
    }

}