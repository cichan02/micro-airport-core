package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.flight.Flight;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final AirportMapper airportMapper;
    private final AirlineMapper airlineMapper;
    private final AirplaneMapper airplaneMapper;

    @SneakyThrows
    public Flight mapRow(ResultSet rs) {
        return new Flight(rs.getLong("flight_id"),
                          rs.getBigDecimal("flight_price"),
                          rs.getTimestamp("flight_departure_time").toLocalDateTime(),
                          rs.getTimestamp("flight_arrival_time").toLocalDateTime());
    }

    @SneakyThrows
    public Flight mapDepartureTimeRow(ResultSet rs) {
        return new Flight(rs.getLong("flight_id"),
                          rs.getTimestamp("flight_departure_time").toLocalDateTime());
    }

    @SneakyThrows
    public Flight mapSearchRow(ResultSet rs) {
        Flight flight = mapRow(rs);

        Airport airportFrom = airportMapper.mapFromAirportRow(rs);
        Airport airportTo = airportMapper.mapToAirportRow(rs);

        Airline airline = airlineMapper.mapRow(rs);

        flight.setTo(airportTo);
        flight.setFrom(airportFrom);
        flight.setAirline(airline);

        return flight;
    }

    @SneakyThrows
    public Flight mapTicketRow(ResultSet rs) {
        Flight flight = mapDepartureTimeRow(rs);

        Airport airportFrom = airportMapper.mapFromAirportRow(rs);
        Airport airportTo = airportMapper.mapToAirportRow(rs);

        Airline airline = airlineMapper.mapRow(rs);

        flight.setTo(airportTo);
        flight.setFrom(airportFrom);
        flight.setAirline(airline);

        return flight;
    }

    @SneakyThrows
    public Flight mapFindRow(ResultSet rs) {
        Flight flight = mapSearchRow(rs);

        Airplane airplane = airplaneMapper.mapRow(rs);

        flight.setAirplane(airplane);

        return flight;
    }

}
