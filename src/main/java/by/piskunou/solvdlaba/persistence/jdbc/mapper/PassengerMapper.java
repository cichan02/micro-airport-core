package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Passenger;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class PassengerMapper {

    @SneakyThrows
    public Passenger rowMap(ResultSet rs) {
        return new Passenger(rs.getLong("passenger_id"),
                             rs.getString("passenger_firstname"),
                             rs.getString("passenger_surname"));
    }
}
