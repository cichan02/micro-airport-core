package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TicketRepository {

    List<Ticket> findAll(long userId);

    Optional<Ticket> findById(@Param("id") long id, @Param("userId") long userId);

    void create(@Param("userId") long userId, @Param("ticket") Ticket ticket);

    boolean isOwner(@Param("ticketId") long ticketId, @Param("userId") long userId);

}
