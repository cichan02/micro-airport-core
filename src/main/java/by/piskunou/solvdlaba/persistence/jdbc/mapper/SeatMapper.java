package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Seat;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class SeatMapper {

    @SneakyThrows
    public Seat rowMap(ResultSet rs) {
        return new Seat(rs.getString("ticket_seat_no"));
    }

}
