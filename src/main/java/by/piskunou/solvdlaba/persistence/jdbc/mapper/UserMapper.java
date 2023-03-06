package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final TicketMapper ticketMapper;

    @SneakyThrows
    public User mapRow(ResultSet rs) {
        return new User(rs.getLong("user_id"),
                        rs.getString("user_name"));
    }

    @SneakyThrows
    public User ticketsMapRow(ResultSet rs) {
        User user = mapRow(rs);
        List<Ticket> tickets = new LinkedList<>();

        rs.previous();
        while(rs.next()) {
            Ticket ticket = ticketMapper.mapRow(rs);
            tickets.add(ticket);
        }

        user.setTickets(tickets);

        return user;
    }

}
