package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class AirplaneMapper {

    @SneakyThrows
    public Airplane mapRow(ResultSet rs) {
        return new Airplane(rs.getLong("airplane_id"),
                            rs.getString("airplane_model"),
                            rs.getByte("airplane_seats_in_row"),
                            rs.getShort("airplane_row_no"));
    }

}