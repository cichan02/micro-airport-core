package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Airline;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class AirlineMapper {

    @SneakyThrows
    public Airline mapRow(ResultSet rs) {
        return new Airline(rs.getLong("airline_id"),
                           rs.getString("airline_name"));
    }

}
