package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.TicketRepository;
import by.piskunou.solvdlaba.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final FlightService flightService;
    private final UserService userService;
    private final PassengerService passengerService;
    private final PassportService passportService;
    private final TicketRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> findAll(long userId) {
        return repository.findAll(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket findById(long id, long userId) {
        elementaryCheckIsEntityValid(userId);
        return repository.findById(id, userId)
                         .orElseThrow(() -> new ResourceNotExistsException("There's no ticket with such id"));
    }

    @Override
    @Transactional
    public Ticket create(long userId, Ticket ticket) {
        fullCheckIsEntityValid(userId, ticket);

        Passenger passenger = ticket.getPassenger();
        long id = passenger.getId() != null ? passenger.getId() : 0;
        if(!passengerService.isExists(id)) {
            Passport passport = passenger.getPassport();

            passengerService.create(passenger);

            passport.setId( passenger.getId() );

            passportService.create(passport);
        }
        flightService.bookSeat(ticket.getFlight().getId(), ticket.getSeat().getNumber());
        repository.create(userId, ticket);
        return ticket;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwner(long ticketId, long userId) {
        return repository.isOwner(ticketId, userId);
    }

    private void elementaryCheckIsEntityValid(long userId) {
        if(!userService.isExists(userId)) {
            throw new ResourceNotExistsException("There's no user with such id");
        }
    }

    private void fullCheckIsEntityValid(long userId, Ticket ticket) {
        elementaryCheckIsEntityValid(userId);
        if(!flightService.isExists(ticket.getFlight().getId())) {
            throw new ResourceNotExistsException("There's no flight with such id");
        }
    }


}
