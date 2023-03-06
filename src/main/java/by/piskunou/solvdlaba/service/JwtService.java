package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String jwt);

    String generateAccessToken(UserDetailsImpl userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateEditPasswordToken(UserDetails userDetails);

    boolean isValidAccessToken(String jwt);

    boolean isValidRefreshToken(String jwt);

    boolean isValidEditPasswordToken(String jwt);

}
