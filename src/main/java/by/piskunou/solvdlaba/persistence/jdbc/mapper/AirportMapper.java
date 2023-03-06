package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Airport;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class AirportMapper {

    @SneakyThrows
    public Airport mapRow(ResultSet rs) {
        return new Airport(rs.getLong("airport_id"),
                           rs.getString("airport_name"));
    }

    @SneakyThrows
    public Airport mapFromAirportRow(ResultSet rs) {
        return new Airport(rs.getLong("airport_from_id"),
                           rs.getString("airport_from_name"));
    }

    @SneakyThrows
    public Airport mapToAirportRow(ResultSet rs) {
        return new Airport(rs.getLong("airport_to_id"),
                           rs.getString("airport_to_name"));
    }

}
