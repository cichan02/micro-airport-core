package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.domain.flight.FlightRequest;
import by.piskunou.solvdlaba.persistence.FlightRepository;
import by.piskunou.solvdlaba.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository repository;

    @Mock
    private AirportService airportService;

    @Mock
    private AirlineService airlineService;

    @Mock
    private AirplaneService airplaneService;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    void verifyFindAllTest() {
        flightService.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        Flight flight = Flight.builder()
                              .id(1L)
                              .from(new Airport(1L))
                              .to(new Airport(2L))
                              .airline(new Airline(2L))
                              .airplane(new Airplane(2L))
                              .departureTime(LocalDateTime.of(2023, 2, 22, 15, 30))
                              .arrivalTime(LocalDateTime.of(2023, 2, 22, 17, 50))
                              .price(BigDecimal.valueOf(150))
                              .build();
        when(repository.findById(1)).thenReturn(Optional.of(flight));
        assertEquals(flight, flightService.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> flightService.findById(Long.MAX_VALUE));
        verify(repository).findById(Long.MAX_VALUE);
    }

    @Test
    void verifyFlightSeatsWithNullFlagTest() {
        List<Seat> seats = new LinkedList<>();
        seats.add(new Seat(1, "1A", true));
        seats.add(new Seat(2, "2A", false));
        when(repository.flightSeats(1)).thenReturn(seats);

        assertEquals(seats, flightService.flightSeats(1, null));
        verify(repository).flightSeats(1);
    }

    @Test
    void verifyFlightSeatsWithTrueFlagTest() {
        List<Seat> seats = new LinkedList<>();
        seats.add(new Seat(1, "1A", true));
        when(repository.flightFreeSeats(1)).thenReturn(seats);

        assertEquals(seats, flightService.flightSeats(1, Boolean.TRUE));
        verify(repository).flightFreeSeats(1);
    }

    @Test
    void verifyFlightSeatsWithFalseFlagTest() {
        List<Seat> seats = new LinkedList<>();
        seats.add(new Seat(2, "2A", false));
        when(repository.flightOccupiedSeats(1)).thenReturn(seats);

        assertEquals(seats, flightService.flightSeats(1, Boolean.FALSE));
        verify(repository).flightOccupiedSeats(1);
    }

    @Test
    void verifySearchOneWatFlightsTest() {
        List<String> from = new LinkedList<>();
        from.add("MSQ");

        List<String> to = new LinkedList<>();
        to.add("DME");
        to.add("SVO");
        to.add("UUWW");

        List<Passenger> passengers = new LinkedList<>();
        passengers.add(new Passenger(Passenger.Age.ADULT));
        passengers.add(new Passenger(Passenger.Age.ADULT));
        passengers.add(new Passenger(Passenger.Age.INFANT));

        FlightRequest request = FlightRequest.builder()
                                             .fromAirports(from)
                                             .toAirports(to)
                                             .passengers(passengers)
                                             .departureDate(LocalDate.of(2023, 2, 27))
                                             .build();

        assertNotNull(flightService.search(request));
        verify(repository).search(from, to, passengers.size(),
                                  LocalDate.of(2023, 2, 27).atStartOfDay(),
                                  LocalDate.of(2023, 2, 27).atTime(LocalTime.MAX));
        verify(repository, times(0)).search(eq(to), eq(from), eq(passengers.size()), any(), any());
    }

    @Test
    void verifySearchReturnFlightsTest() {
        List<String> from = new LinkedList<>();
        from.add("MSQ");

        List<String> to = new LinkedList<>();
        to.add("DME");
        to.add("SVO");
        to.add("UUWW");

        List<Passenger> passengers = new LinkedList<>();
        passengers.add(new Passenger(Passenger.Age.ADULT));
        passengers.add(new Passenger(Passenger.Age.ADULT));
        passengers.add(new Passenger(Passenger.Age.INFANT));

        FlightRequest request = FlightRequest.builder()
                .fromAirports(from)
                .toAirports(to)
                .passengers(passengers)
                .departureDate(LocalDate.of(2023, 2, 27))
                .arrivalDate(LocalDate.of(2023, 3, 4))
                .build();

        assertNotNull(flightService.search(request));
        verify(repository).search(from, to, passengers.size(),
                LocalDate.of(2023, 2, 27).atStartOfDay(),
                LocalDate.of(2023, 2, 27).atTime(LocalTime.MAX));
        verify(repository).search(to, from, passengers.size(),
                LocalDate.of(2023, 3, 4).atStartOfDay(),
                LocalDate.of(2023, 3, 4).atTime(LocalTime.MAX));
    }


    @Test
    void verifyCreateTest() {
        Airplane airplane = Airplane.builder()
                                    .id(4L)
                                    .model("A320")
                                    .seatsInRow((byte) 6)
                                    .rowNo((short) 32)
                                    .build();
        Flight flight = Flight.builder()
                              .from(new Airport(1L))
                              .to(new Airport(2L))
                              .airline(new Airline(3L))
                              .airplane(new Airplane(4L))
                              .departureTime(LocalDateTime.of(2023, 2, 22, 15, 30))
                              .arrivalTime(LocalDateTime.of(2023, 2, 22, 17, 50))
                              .price(BigDecimal.valueOf(150))
                              .build();
        when(airportService.isExists(1)).thenReturn(true);
        when(airportService.isExists(2)).thenReturn(true);
        when(airlineService.isExists(3L)).thenReturn(true);
        when(airplaneService.isExists(4)).thenReturn(true);
        when(airplaneService.findById(4)).thenReturn(airplane);
        doAnswer(invocation -> {
            Flight flight1 = invocation.getArgument(0);
            flight1.setId(1L);
            return null;
        }).when(repository).create(flight);

        assertEquals(flight, flightService.create(flight));
        verify(airportService).isExists(1);
        verify(airportService).isExists(2);
        verify(airlineService).isExists(3L);
        verify(airplaneService).isExists(4);
        verify(airplaneService).findById(4);
        verify(repository).create(flight);
        assertEquals(1, flight.getId());
    }

    @Test
    void verifyCreateByNonexistentFromIdTest() {
        Flight flight = Flight.builder()
                              .from(new Airport(1L))
                              .build();

        assertThrows(ResourceNotExistsException.class, () -> flightService.create(flight));
        verify(airportService).isExists(1);
    }

    @Test
    void verifyCreateByNonexistentToIdTest() {
        Flight flight = Flight.builder()
                            .from(new Airport(1L))
                            .to(new Airport(2L))
                            .build();
        when(airportService.isExists(1)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> flightService.create(flight));
        verify(airportService).isExists(1);
        verify(airportService).isExists(2);
    }

    @Test
    void verifyCreateByNonexistentAirlineIdTest() {
        Flight flight = Flight.builder()
                .from(new Airport(1L))
                .to(new Airport(2L))
                .airline(new Airline(3L))
                .build();
        when(airportService.isExists(1)).thenReturn(true);
        when(airportService.isExists(2)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> flightService.create(flight));
        verify(airportService).isExists(1);
        verify(airportService).isExists(2);
        verify(airlineService).isExists(3L);
    }

    @Test
    void verifyCreateByNonexistentAirplaneIdTest() {
        Flight flight = Flight.builder()
                            .from(new Airport(1L))
                            .to(new Airport(2L))
                            .airline(new Airline(3L))
                            .airplane(new Airplane(4L))
                            .build();
        when(airportService.isExists(1)).thenReturn(true);
        when(airportService.isExists(2)).thenReturn(true);
        when(airlineService.isExists(3L)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> flightService.create(flight));
        verify(airportService).isExists(1);
        verify(airportService).isExists(2);
        verify(airlineService).isExists(3L);
        verify(airplaneService).isExists(4);
    }

    @Test
    void verifyUpdateByIdTest() {
        Flight flight = Flight.builder()
                              .airline(new Airline(2L))
                              .departureTime(LocalDateTime.of(2023, 2, 22, 15, 30))
                              .arrivalTime(LocalDateTime.of(2023, 2, 22, 17, 50))
                              .price(BigDecimal.valueOf(150))
                              .build();
        when(flightService.isExists(1)).thenReturn(true);
        when(airlineService.isExists(2L)).thenReturn(true);

        assertEquals(flight, flightService.updateById(1, flight));
        verify(repository).isExistsById(1);
        assertEquals(1, flight.getId());
        verify(airlineService).isExists(2L);
        verify(repository).update(flight);
    }

    @Test
    void verifyUpdateByNonexistentAirlineIdTest() {
        Flight flight = Flight.builder()
                              .airline(new Airline(Long.MAX_VALUE))
                              .build();
        when(flightService.isExists(1)).thenReturn(true);

        assertThrows(ResourceNotExistsException.class, () -> flightService.updateById(1, flight));
        verify(repository).isExistsById(1);
        assertEquals(1, flight.getId());
        verify(airlineService).isExists(Long.MAX_VALUE);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        assertThrows(ResourceNotExistsException.class, () -> flightService.updateById(Long.MAX_VALUE, any()));
        verify(repository).isExistsById(Long.MAX_VALUE);
    }

    @Test
    void bookSeat() {
        flightService.bookSeat(1, 1);
        verify(repository).bookSeat(1L, 1);
    }

    @Test
    void removeById() {
        flightService.removeById(1);
        verify(repository).removeById(1);
    }

    @Test
    void verifyIsExistsTest() {
        assertFalse(flightService.isExists(1));
        verify(repository).isExistsById(1);
    }


}