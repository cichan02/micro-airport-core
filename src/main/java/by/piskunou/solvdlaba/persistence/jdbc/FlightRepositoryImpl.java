package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.FlightRepository;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.FlightMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Repository
@RequiredArgsConstructor
public class FlightRepositoryImpl implements FlightRepository {

    private final DataSourceConfig config;
    private final FlightMapper flightMapper;
    private final Gson gson;

    private static final String FIND_BY_ID = """
            select flights.id as flight_id,
                   flights.departure_time as flight_departure_time,
                   flights.arrival_time as flight_arrival_time,
                   flights.price as flight_price,
                       
                   airport_from.id as airport_from_id,
                   airport_from.name as airport_from_name,
                       
                   airport_to.id as airport_to_id,
                   airport_to.name as airport_to_name,
                       
                   airplanes.id as airplane_id,
                   airplanes.model as airplane_model,
                   airplanes.seats_in_row as airplane_seats_in_row,
                   airplanes.row_no as airplane_row_no,
                       
                   airlines.id as airline_id,
                   airlines.name as airline_name
                   
            from flights inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
                        inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
                        inner join airlines on flights.fk_airline_id = airlines.id
                        inner join airplanes on flights.fk_airplane_id = airplanes.id
            where flights.id = ?;
            """;

    private static final String CREATE = """
            insert into flights(fk_airport_from_id,
                               fk_airport_to_id,
                               fk_airline_id,
                               fk_airplane_id,
                               departure_time,
                               arrival_time,
                               price,
                               free_seats)
            values (?, ?, ?, ?, ?, ?, ?, ?::jsonb)
            """;

    private static final String SEARCH = """
            select flights.id as flight_id,
                    flights.departure_time as flight_departure_time,
                    flights.arrival_time as flight_arrival_time,
                    flights.price as flight_price,
                    
                    airport_from.id as airport_from_id,
                    airport_from.name as airport_from_name,
                    
                    airport_to.id as airport_to_id,
                    airport_to.name as airport_to_name,
                    
                    airlines.id as airline_id,
                    airlines.name as airline_name
                       
            from flights inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
                        inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
                        inner join airlines on flights.fk_airline_id = airlines.id
            where airport_from.id in (?) and airport_to.id in (?) and
                  flights.departure_time between ? and ?""";

    private static final String BOOK_SEAT = "";

    private static final String FREE_SEATS = """
            select id as flight_id,
                   jsonb_path_query_array(flights.free_seats, '$[*] ? (@.free == true)') as free_seats
            from flights
            where flights.id = ?""";

    private static final String EXISTS_BY_ID = "select exists (select from flights where id= ?)";


    @Override
    public List<Flight> findAll() {
        return Collections.emptyList();
    }

    @Override
    @SneakyThrows
    public Optional<Flight> findById(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Flight flight = null;
                if (resultSet.next()) {
                    flight = flightMapper.mapFindRow(resultSet);
                }
                return Optional.ofNullable(flight);
            }
        }
    }

    @Override
    @SneakyThrows
    public void create(Flight flight) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement
                    = conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, flight.getFrom().getId());
            preparedStatement.setLong(2, flight.getTo().getId());
            preparedStatement.setLong(3, flight.getAirline().getId());
            preparedStatement.setLong(4, flight.getAirplane().getId());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(flight.getDepartureTime()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(flight.getArrivalTime()));
            preparedStatement.setBigDecimal(7, flight.getPrice());

            Type listType = new TypeToken<List<Seat>>(){}.getType();
            String json = gson.toJson(flight.getSeats(), listType);

            preparedStatement.setObject(8, json);
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    flight.setId(id);
                }
            }
        }
    }

    @Override
    public void update(Flight flight) {

    }

    @Override
    public void bookSeat(Long id, int number) {

    }

    @Override
    public void removeById(long id) {

    }

    @Override
    @SneakyThrows
    public List<Flight> search(List<String> fromAirports, List<String> toAirports,
                               int passengerAmount, LocalDateTime start, LocalDateTime end) {
        Connection conn = config.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement(SEARCH,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setObject(1, fromAirports.stream()
                                                                    .map(String::valueOf)
                                                                    .collect(Collectors.joining(", ")));
            preparedStatement.setObject(2, toAirports.stream()
                                                                   .map(String::valueOf)
                                                                   .collect(Collectors.joining(", ")));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(start));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(end));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Flight> flights = new LinkedList<>();
                while (resultSet.next()) {
                    Flight flight = flightMapper.mapSearchRow(resultSet);
                    flights.add(flight);
                }
                return flights;
            }
        }
    }

    @Override
    @SneakyThrows
    public List<Seat> flightSeats(long id) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FREE_SEATS)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                resultSet.next();
                Type listType = new TypeToken<List<Seat>>() {}.getType();

                String json = resultSet.getString("free_seats");
                return gson.fromJson(json, listType);
            }
        }
    }

    @Override
    public List<Seat> flightFreeSeats(long id) {
        return Collections.emptyList();
    }

    @Override
    public List<Seat> flightOccupiedSeats(long id) {
        return Collections.emptyList();
    }

    @Override
    @SneakyThrows
    public boolean isExistsById(long id) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(EXISTS_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean("exists");
            }
        }
    }

}
