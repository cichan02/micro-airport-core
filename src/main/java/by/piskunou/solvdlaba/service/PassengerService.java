package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Passenger;

public interface PassengerService {

    Passenger create (Passenger passenger);

    boolean isExists(long id);

}
