package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airport;

public interface AirportService {

    Airport findById(long id);

    boolean isExists(long id);

}
