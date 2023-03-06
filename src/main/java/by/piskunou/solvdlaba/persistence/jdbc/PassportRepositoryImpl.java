package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.PassportRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//@Repository
@RequiredArgsConstructor
public class PassportRepositoryImpl implements PassportRepository {

    private final DataSourceConfig config;

    private static final String CREATE = """
            insert into passports(fk_passenger_id, \"number\", identification_no)
            values (?, ?, ?)""";

    @Override
    @SneakyThrows
    public void create(Passport passport) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, passport.getId());
            preparedStatement.setString(2, passport.getNumber());
            preparedStatement.setString(3, passport.getIdentificationNo());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    passport.setId(id);
                }
            }
        }
    }

}
