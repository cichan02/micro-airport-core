package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import by.piskunou.solvdlaba.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("There's no user with such username");
        }
        return new UserDetailsImpl(user.get());
    }

}
