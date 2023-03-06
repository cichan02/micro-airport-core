package by.piskunou.solvdlaba.persistence.jdbc;

import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.persistence.DataSourceConfig;
import by.piskunou.solvdlaba.persistence.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final DataSourceConfig config;

    private static final String CREATE = """
            insert into tickets(fk_owner_id, fk_flight_id, fk_passenger_id, seat_no)
            values (?, ?, ?, ?)""";

    @Override
    @SneakyThrows
    public void create(long userId, Ticket ticket) {
        Connection conn = config.getConnection();

        try(PreparedStatement preparedStatement =
                    conn.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, ticket.getFlight().getId());
            preparedStatement.setLong(3, ticket.getPassenger().getId());
            preparedStatement.setString(4, ticket.getSeat().getPlace());
            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if(resultSet.next()) {
                    Long id = resultSet.getLong("id");

                    ticket.setId(id);
                }
            }
        }
    }

    @Override
    public List<Ticket> findAll(long userId) {
         return Collections.emptyList();
    }

    @Override
    public Optional<Ticket> findById(long id, long userId) {
        return Optional.empty();
    }

    @Override
    public boolean isOwner(long ticketId, long userId) {
        return false;
    }

}
