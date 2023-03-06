package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.domain.UserDetailsImpl;
import by.piskunou.solvdlaba.persistence.UserRepository;
import by.piskunou.solvdlaba.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    void verifyLoadUserByUsernameTest() {
        User user = User.builder()
                        .id(1L)
                        .username("Agata")
                        .password("1234")
                        .role(User.Role.USER)
                        .build();
        when(repository.findByUsername("Agata")).thenReturn(Optional.of(user));
        UserDetailsImpl userDetails = (UserDetailsImpl) service.loadUserByUsername("Agata");
        assertEquals(user, userDetails.getUser());
        verify(repository).findByUsername("Agata");
    }

    @Test
    void verifyLoadUserByUsernameFailedTest() {
        when(repository.findByUsername("Robert")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("Robert"));
        verify(repository).findByUsername("Robert");
    }

}