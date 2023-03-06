package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.persistence.CountryRepository;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.jdbc.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final DataSourceConfig config;
    private final CountryMapper countryMapper;

    private static final String FIND_BY_ID = """
            select id as country_id,
                   name as country_name
            from countries where id = ?""";

    private static final String FIND_BY_NAME = """
            select id as country_id,
                   name as country_name
            from countries where name = ?""";

    private static final String FIND_CITIES_BY_ID = """
        select countries.id as country_id,
               countries.name as country_name,
               cities.id as city_id,
               cities.name as city_name
        from countries left join cities on countries.id = cities.fk_country_id 
        where countries.id = ?""";

    private static final String FIND_AIRPORTS_BY_ID = """
        select countries.id as country_id,
               countries.name as country_name,
               cities.id as city_id,
               cities.name as city_name,
               airports.id as airport_id,
               airports.name as airport_name
        from countries left join cities on countries.id = cities.fk_country_id
                     left join airports on cities.id = airports.fk_city_id
        where countries.id = ?""";

    private static final String FIND_ALL = """
            select id as country_id,
                   name as country_name
            from countries""";

    private static final String CREATE = "insert into countries(name) values(?)";
    private static final String UPDATE = "update countries set name = ? where id = ?";
    private static final String DELETE = "delete from countries where id = ?";
    private static final String EXISTS_BY_ID = "select exists (select from countries where id= ?)";
    private static final String EXISTS_BY_NAME = "select exists (select from countries where name= ?)";

    @Override
    @SneakyThrows
    public List<Country> findAll() {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Country> countries = new LinkedList<>();
            while (resultSet.next()) {
                Country country = countryMapper.mapRow(resultSet);
                countries.add(country);
            }
            return countries;
        }
    }

    @Override
    @SneakyThrows
    public Optional<Country> findById(long id) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Country country = null;
                if (resultSet.next()) {
                    country = countryMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(country);
            }
        }
    }

    @SneakyThrows
    public Optional<Country> findByName(String name) {
        Connection conn = config.getConnection();

        try (PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, name);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Country country = null;
                if(resultSet.next()) {
                    country = countryMapper.mapRow(resultSet);
                }
                return Optional.ofNullable(country);
            }
        }
    }

    @SneakyThrows
    public Optional<Country> findCountryCities(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_CITIES_BY_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Country country = null;
                if(resultSet.next()) {
                    country = countryMapper.citiesMapRow(resultSet);
                }
                return Optional.ofNullable(country);
            }
        }
    }

    @SneakyThrows
    public Optional<Country> findCountryAirports(long id) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement = conn.prepareStatement(FIND_AIRPORTS_BY_ID,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setLong(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                Country country = null;
                if(resultSet.next()) {
                    country = countryMapper.airportsMapRow(resultSet);
                }
                return Optional.ofNullable(country);
            }
        }
    }

    @Override
    @SneakyThrows
    public void create(Country country) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, country.getName());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    country.setId(id);
                }
            }
        }
    }

    @Override
    public void update(Country country) {}

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
