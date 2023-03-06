package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.domain.flight.FlightRequest;
import by.piskunou.solvdlaba.domain.flight.FlightResponse;

import java.util.List;

public interface FlightService {

    List<Flight> findAll();

    Flight findById(long id);

    List<FlightResponse> search(FlightRequest flightRequest);

    List<Seat> flightSeats(long id, Boolean free);

    Flight create(Flight flight);

    Flight updateById(long id, Flight flight);

    void bookSeat(long id, int number);

    void removeById(long id);

    boolean isExists(long id);

}
