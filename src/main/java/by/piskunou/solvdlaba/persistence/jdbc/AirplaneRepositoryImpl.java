package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import by.piskunou.solvdlaba.persistence.AirplaneRepository;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.AirplaneMapper;
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
public class AirplaneRepositoryImpl implements AirplaneRepository {

    private final DataSourceConfig config;
    private final AirplaneMapper mapper;

    private static final String FIND_BY_ID = """
            select id as airplane_id,
                   model as airplane_model,
                   seats_in_row as airplane_seats_in_row,
                   row_no as airplane_row_no
            from airplanes where id = ?""";

    private static final String FIND_BY_MODEL = """
            select id as airplane_id,
                   model as airplane_model,
                   seats_in_row as airplane_seats_in_row,
                   row_no as airplane_row_no
            from airplane where model = ?""";

    private static final String FIND_ALL = """
             select id as airplane_id,
                    model as airplane_model,
                    seats_in_row as airplane_seats_in_row,
                    row_no as airplane_row_no
             from airplanes""";

    private static final String CREATE = "insert into airplanes(model, seats_in_row, row_no) values(?, ?, ?)";
    private static final String DELETE = "delete from airplanes where id = ?";
    private static final String EXISTS_BY_MODEL = "select exists (select from airplanes where model = ?)";
    private static final String EXISTS_BY_ID = "select exists (select from airplanes where id = ?)";

    @Override
    @SneakyThrows
    public void create(Airplane airplane) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, airplane.getModel());
            preparedStatement.setByte(2, airplane.getSeatsInRow());
            preparedStatement.setShort(3, airplane.getRowNo());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    airplane.setId(id);
                }
            }
        }
    }

    @Override
    public void update(Airplane airplane) {}

    @Override
    public List<Airplane> search(AirplaneRequest request) {
        return Collections.emptyList();
    }

    @Override
    @SneakyThrows
    public Optional<Airplane> findById(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Airplane airplane = null;
                if(resultSet.next()) {
                    airplane = mapper.mapRow(resultSet);
                }
                return Optional.ofNullable(airplane);
            }
        }
    }

    @Override
    @SneakyThrows
    public List<Airplane> findAll() {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Airplane> airplanes = new LinkedList<>();

            while (resultSet.next()) {
                Airplane airplane = mapper.mapRow(resultSet);
                airplanes.add(airplane);
            }
            return airplanes;
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
    public boolean isExistsByModel(Long id, String model) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(EXISTS_BY_MODEL)) {
            preparedStatement.setString(1, model);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean("exists");
            }
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

}
