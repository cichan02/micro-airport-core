package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    //todo: rewrite
    @Override
    public Airport findById(long id) {
        return null;
    }

    //todo: rewrite
    @Override
    public boolean isExists(long id) {
        return false;
    }

}
