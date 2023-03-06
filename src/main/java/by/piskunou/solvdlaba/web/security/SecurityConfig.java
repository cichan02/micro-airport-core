package by.piskunou.solvdlaba.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity https) throws Exception {
        return https.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/sign-up").permitAll()
                .requestMatchers("/airlines",
                                           "/airlines/*",
                                           "/airplanes",
                                           "/airplanes/*",
                                           "/airports",
                                           "/airports/*",
                                           "/countries",
                                           "/countries/*").hasAuthority("ADMIN")
                .requestMatchers("/refresh",
                                           "/login",
                                           "/swagger-ui/*",
                                           "/v3/api-docs*",
                                           "/cities/search",
                                           "/password/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/logout")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
