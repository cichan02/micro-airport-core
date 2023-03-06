package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import by.piskunou.solvdlaba.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt-secret}")
    private String secretKey;

    @Override
    public String extractUsername(String jwt) {
        DecodedJWT decodedJwt = JWT.decode(jwt);
        return decodedJwt.getClaim("username").asString();
    }

    @Override
    public String generateAccessToken(UserDetailsImpl userDetails) {
        return JWT.create()
                  .withSubject("Access token")
                  .withClaim("username", userDetails.getUsername())
                  .withClaim("email", userDetails.getUser().getEmail())
                  .withIssuer("Airport")
                  .withIssuedAt(Instant.now())
                  .withExpiresAt( ZonedDateTime.now().plusHours(1).toInstant() )
                  .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return JWT.create()
                  .withSubject("Refresh token")
                  .withClaim("username", userDetails.getUsername())
                  .withIssuer("Airport")
                  .withIssuedAt(Instant.now())
                  .withExpiresAt( ZonedDateTime.now().plusWeeks(1).toInstant() )
                  .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public String generateEditPasswordToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject("Edit password token")
                .withClaim("username", userDetails.getUsername())
                .withIssuer("Airport")
                .withIssuedAt(Instant.now())
                .withExpiresAt( ZonedDateTime.now().plusMinutes(5).toInstant() )
                .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public boolean isValidAccessToken(String jwt) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                                      .withSubject("Access token")
                                      .withIssuer("Airport")
                                      .build();
            DecodedJWT decodedJwt = verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public boolean isValidRefreshToken(String jwt) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                                      .withSubject("Refresh token")
                                      .withIssuer("Airport")
                                      .build();
            DecodedJWT decodedJwt = verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public boolean isValidEditPasswordToken(String jwt) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                                      .withSubject("Edit password token")
                                      .withIssuer("Airport")
                                      .build();
            DecodedJWT decodedJwt = verifier.verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

}
