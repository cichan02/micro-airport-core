package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.persistence.PassportRepository;
import by.piskunou.solvdlaba.service.impl.PassportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassportServiceTest {

    @Mock
    private PassportRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private PassportServiceImpl service;

    @Test
    void verifyCreateTest() {
        Passport passport = Passport.builder()
                                    .id(6L)
                                    .number("1234")
                                    .identificationNo("1234")
                                    .build();
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Passport passport = invocation.getArgument(0);
                passport.setId(1L);
                return null;
            }
        }).when(repository).create(passport);

        assertEquals(passport, service.create(passport));
        verify(encoder, times(2)).encode(any());
        assertEquals(encoder.encode("1234"), passport.getNumber());
        assertEquals(encoder.encode("1234"), passport.getIdentificationNo());
        verify(repository).create(passport);
        assertEquals(1, passport.getId());
    }

}