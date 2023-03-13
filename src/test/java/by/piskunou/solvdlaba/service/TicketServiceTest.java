package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.persistence.TicketRepository;
import by.piskunou.solvdlaba.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private FlightService flightService;

    @Mock
    private UserService userService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private PassportService passportService;

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void verifyFindAllTest() {
        ticketService.findAll(1);
        verify(repository).findAll(1);
    }

    @Test
    void verifyFindByIdTest() {
        Ticket ticket = Ticket.builder()
                              .id(45L)
                              .passenger(new Passenger(2L))
                              .flight(new Flight(3L))
                              .seat(new Seat(1, "1A"))
                              .build();
        when(userService.isExists(1)).thenReturn(true);
        when(repository.findById(45, 1)).thenReturn(Optional.of(ticket));

        assertEquals(ticket, ticketService.findById(45, 1));
        verify(repository).findById(45, 1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        when(userService.isExists(1)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> ticketService.findById(Long.MAX_VALUE, 1));
        verify(repository).findById(Long.MAX_VALUE, 1);
    }

    @Test
    void verifyCreateTest() {
        Ticket ticket = Ticket.builder()
                              .passenger(new Passenger(2L))
                              .flight(new Flight(3L))
                              .seat(new Seat(1))
                              .build();
        when(userService.isExists(1)).thenReturn(true);
        when(flightService.isExists(3)).thenReturn(true);
        when(passengerService.isExists(2)).thenReturn(true);
        doAnswer(invocation -> {
            Ticket ticket1 = invocation.getArgument(1);
            ticket1.setId(10L);
            return null;
        }).when(repository).create(1, ticket);

        assertEquals(ticket, ticketService.create(1, ticket));
        verify(userService).isExists(1);
        verify(flightService).isExists(3);
        verify(passengerService).isExists(2);
        verify(flightService).bookSeat(3, 1);
        verify(repository).create(1, ticket);
        assertEquals(10, ticket.getId());
    }

    @Test
    void verifyCreateNonexistentUserIdTest() {
        assertThrows(ResourceNotExistsException.class, () -> ticketService.create(1, any()));
        verify(userService).isExists(1);
    }

    @Test
    void verifyCreateNonexistentFlightIdTest() {
        Ticket ticket = Ticket.builder()
                              .flight(new Flight(3L))
                              .build();
        when(userService.isExists(1)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> ticketService.create(1, ticket));
        verify(userService).isExists(1);
        verify(flightService).isExists(3);
    }

    @Test
    void verifyCreateNonexistentPassengerTest() {
        Passport passport = Passport.builder()
                                    .id(1L)
                                    .number("1234")
                                    .identificationNo("1234")
                                    .build();
        Passenger passenger = Passenger.builder()
                                       .passport(passport)
                                       .build();
        Ticket ticket = Ticket.builder()
                              .passenger(passenger)
                              .flight(new Flight(3L))
                              .seat(new Seat(1))
                              .build();
        when(userService.isExists(1)).thenReturn(true);
        when(flightService.isExists(3)).thenReturn(true);

        assertEquals(ticket, ticketService.create(1, ticket));
        verify(userService).isExists(1);
        verify(flightService).isExists(3);
        verify(passengerService).isExists(0);
        verify(passengerService).create(passenger);
        assertNull(passport.getId());
        verify(passportService).create(passport);
        verify(flightService).bookSeat(3, 1);
        verify(repository).create(1, ticket);
    }

    @Test
    void verifyIsOwnerTest() {
        ticketService.isOwner(45, 1);
        verify(repository).isOwner(45, 1);
    }
    
}