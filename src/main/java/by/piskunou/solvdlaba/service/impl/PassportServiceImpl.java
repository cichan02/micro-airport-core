package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.persistence.PassportRepository;
import by.piskunou.solvdlaba.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository repository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public Passport create(Passport passport) {
        passport.setNumber( encoder.encode(passport.getNumber()) );
        passport.setIdentificationNo( encoder.encode(passport.getIdentificationNo()) );
        repository.create(passport);
        return passport;
    }

}
