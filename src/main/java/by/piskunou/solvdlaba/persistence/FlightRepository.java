package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.flight.Flight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface FlightRepository {

    List<Flight> findAll();

    Optional<Flight> findById(long id);

    List<Flight> search(@Param("fromAirports") List<String> fromAirports, @Param("toAirports") List<String> toAirports,
                        @Param("passengerAmount") int passengerAmount,
                        @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Seat> flightSeats(long id);

    List<Seat> flightFreeSeats(long id);

    List<Seat> flightOccupiedSeats(long id);

    void create(Flight flight);

    void update(Flight flight);

    void bookSeat(@Param("id") Long id, @Param("number") int number);

    void removeById(long id);

    boolean isExistsById(long id);

}
