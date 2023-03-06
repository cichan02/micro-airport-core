package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> findAll(long userId);

    Ticket findById(long id, long userId);

    Ticket create(long userId, Ticket ticket);

    boolean isOwner(long ticketId, long userId);

}
