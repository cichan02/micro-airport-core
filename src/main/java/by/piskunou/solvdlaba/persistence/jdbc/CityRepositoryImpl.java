package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.persistence.CityRepository;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.CityMapper;
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
public class CityRepositoryImpl implements CityRepository {

    private final DataSourceConfig config;
    private final CityMapper cityMapper;

    private static final String FIND_BY_ID = """
             select id as city_id,
                    name as city_name
             from cities where id = ?""";

    private static final String FIND_AIRPORTS_BY_ID = """
            select cities.id as city_id,
                   cities.name as city_name,
                   airports.id as airport_id,
                   airports.name as airport_name
            from cities left join airports on cities.id = airports.fk_city_id
            where cities.id = ?""";

    private static final String FIND_ALL = """
             select id as city_id,
                    name as city_name
             from cities""";

    private static final String CREATE = "insert into cities(fk_country_id, name) values(?, ?)";
    private static final String UPDATE = "update cities set name = ? where id = ?";
    private static final String DELETE = "delete from cities where id = ?";
    private static final String EXISTS_BY_ID = "select exists (select from cities where id = ?)";
    private static final String EXISTS_BY_NAME = "select exists (select from cities where name = ?)";

    @Override
    @SneakyThrows
    public List<City> findAll() {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<City> cities = new LinkedList<>();

            while (resultSet.next()) {
                City city = cityMapper.mapRow(resultSet);
                cities.add(city);
            }
            return cities;
        }
    }

    @Override
    @SneakyThrows
    public Optional<City> findById(long id) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                City city =  null;
                if (resultSet.next()) {
                    city = cityMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(city);
            }
        }
    }

    @Override
    @SneakyThrows
    public Optional<City> findByIdWithAirports(long id) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_AIRPORTS_BY_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                City city = null;
                if (resultSet.next()) {
                    city = cityMapper.airportsMapRow(resultSet);
                }
                return Optional.ofNullable(city);
            }
        }
    }

    @Override
    public List<City> search(String inquiry) {
        return Collections.emptyList();
    }

    @Override
    @SneakyThrows
    public void create(long countryId, City city) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement =
                     conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, countryId);
            preparedStatement.setString(2, city.getName());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    city.setId(id);
                }
            }
        }
    }

    @Override
    public void update(long countryId, City city) {}

    @SneakyThrows
    public void updateNameById(long id, String updatedName) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, updatedName);
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

}
