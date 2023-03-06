package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.service.impl.JwtServiceImpl;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl service;

    @Test
    void extractUsername() {
        assertThrows(JWTVerificationException.class, () -> service.extractUsername("invalidJwt"));
    }

}