package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.persistence.AirlineRepository;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.AirlineMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class AirlineRepositoryImpl implements AirlineRepository {

    private final DataSourceConfig config;
    private final AirlineMapper airlineMapper;

    private final static String FIND_ALL = """
        select id as airline_id,
               name as airline_name,
               iata as airline_iata_code,
               icao as airline_icao_code,
               callsign as airline_callsign
        from airlines""";

    private final static String FIND_BY_ID = """
        select id as airline_id,
               name as airline_name,
               iata as airline_iata_code,
               icao as airline_icao_code,
               callsign as airline_callsign
        from airlines
        where id = ?""";

    private final static String SEARCH = """
            select id as airline_id,
                   name as airline_name,
                   iata as airline_iata_code,
                   icao as airline_icao_code,
                   callsign as airline_callsign
            from airlines
            where name like ? and
                  iata like ? and
                  icao like ? and
                  callsign like ?""";

    private static final String CREATE = "insert into airlines(name, iata, icao, callsign) values(?, ?, ?, ?)";
    private static final String UPDATE = """
        update airlines set name = ?,
                            iata = ?,
                            icao = ?,
                            callsign = ?
        where id = ?""";

    private static final String DELETE = "delete from airlines where id = ?";
    private static final String EXISTS_BY_ID = "select exists (select from airlines where id = ?)";
    private static final String EXISTS_BY_NAME = "select exists (select from airlines where name = ?)";

    @Override
    @SneakyThrows
    public List<Airline> findAll() {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Airline> airlines = new LinkedList<>();

            while (resultSet.next()) {
                Airline airline = airlineMapper.mapRow(resultSet);
                airlines.add(airline);
            }
            return airlines;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Airline> findById(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Airline airline = null;
                if(resultSet.next()) {
                    airline = airlineMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(airline);
            }
        }
    }

    @Override
    public List<Airline> search(Airline airline) {
        return Collections.emptyList();
    }

    @Override
    @SneakyThrows
    public void create(Airline airline) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, airline.getName());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    airline.setId(id);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public void update(Airline airline) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, airline.getName());
            preparedStatement.setLong(2, airline.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows
    public void removeById(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        }
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

    @Override
    @SneakyThrows
    public boolean isExistsByName(Long id, String name) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(EXISTS_BY_NAME)) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean("exists");
            }
        }
    }

    @Override
    public boolean isExistsByIata(Long id, String iata) {
        return false;
    }

    @Override
    public boolean isExistsByIcao(Long id, String icao) {
        return false;
    }

    @Override
    public boolean isExistsByCallsign(Long id, String callsign) {
        return false;
    }

}
