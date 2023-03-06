package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.domain.flight.Flight;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final SeatMapper seatMapper;
    private final FlightMapper flightMapper;
    private final PassengerMapper passengerMapper;

    @SneakyThrows
    public Ticket mapRow(ResultSet rs) {
        Seat seat = seatMapper.rowMap(rs);
        Flight flight = flightMapper.mapTicketRow(rs);
        Passenger passenger = passengerMapper.rowMap(rs);

        return new Ticket(rs.getLong("ticket_id"),
                          passenger, flight, seat);
    }

}
