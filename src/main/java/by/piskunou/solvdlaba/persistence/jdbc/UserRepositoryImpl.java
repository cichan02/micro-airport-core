package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.UserRepository;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.UserMapper;
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
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig config;
    private final UserMapper userMapper;

    private static final String FIND_ALL = """
            select id as user_id,
                    username as user_name
            from users""";

    private static final String FIND_BY_ID = """
        select id as user_id,
               username as user_name
        from users where id = ?""";

    private static final String FIND_BY_USERNAME = """
            select id as user_id,
                   username as user_name
            from users where username = ?""";

    private static final String FIND_TICKETS_BY_ID = """
        select users.id as user_id,
               users.username as user_name,
        
               tickets.id as ticket_id,
               tickets.seat_no as ticket_seat_no,
        
               passengers.id as passenger_id,
               passengers.firstname as passenger_firstname,
               passengers.surname as passenger_surname,
        
               flights.id as flight_id,
               flights.departure_time as flight_departure_time,
        
               airport_from.id as airport_from_id,
               airport_from.name as airport_from_name,
        
               airport_to.id as airport_to_id,
               airport_to.name as airport_to_name,
        
               airlines.id as airline_id,
               airlines.name airline_name
        
        from users left join tickets on users.id = tickets.fk_owner_id
                  inner join passengers on tickets.fk_passenger_id = passengers.id
                  inner join flights on tickets.fk_flight_id = flights.id
                  inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
                  inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
                  inner join airlines on flights.fk_airline_id = airlines.id
        where users.id = ?""";

    private static final String REGISTER = "insert into users (username) values(?)";
    private static final String UPDATE = "update users set username = ? where id = ?";
    private static final String DELETE = "delete from users where id = ?";
    private static final String EXISTS_BY_ID = "select exists (select from users where id = ?)";
    private static final String EXISTS_BY_NAME = "select exists (select from users where username = ?)";
    private static final String EXISTS_BY_EMAIL = "select exists (select from users where email = ?)";

    @SneakyThrows
    public void register(User user) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(REGISTER, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    user.setId(id);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public Optional<User> findById(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                User user = null;
                if(resultSet.next()) {
                    user = userMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(user);
            }
        }
    }

    @Override
    public Optional<User> findByIdWithTickets(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> search(User user) {
        return Collections.emptyList();
    }

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void updatePasswordById(long id, String newPassword) {

    }

    @Override
    public void updatePasswordByUsername(String username, String password) {

    }

    @SneakyThrows
    public Optional<User> findByUsername(String username) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_USERNAME)) {
            preparedStatement.setString(1, username);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                User user = null;
                if(resultSet.next()) {
                    user = userMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(user);
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public String findPasswordById(long id) {
        return "";
    }

    @Override
    @SneakyThrows
    public List<User> findAll() {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            List<User> users = new LinkedList<>();
            while (resultSet.next()) {
                User user = userMapper.mapRow(resultSet);
                users.add(user);
            }
            return users;
        }
    }

    @SneakyThrows
    public Optional<User> findUserTickets(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_TICKETS_BY_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                User user = null;
                if(resultSet.next()) {
                    user = userMapper.ticketsMapRow(resultSet);
                }
                return Optional.ofNullable(user);
            }
        }
    }

    @SneakyThrows
    public void updateUsernameById(long id, String username) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, username);
            preparedStatement.setLong(2, id);

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
    public boolean isExistsByUsername(Long id, String name) {
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
    @SneakyThrows
    public boolean isExistsByEmail(Long id, String email) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(EXISTS_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean("exists");
            }
        }
    }

}
