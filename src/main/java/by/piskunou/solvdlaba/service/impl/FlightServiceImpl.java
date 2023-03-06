package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.domain.flight.FlightRequest;
import by.piskunou.solvdlaba.domain.flight.FlightResponse;
import by.piskunou.solvdlaba.persistence.FlightRepository;
import by.piskunou.solvdlaba.service.AirlineService;
import by.piskunou.solvdlaba.service.AirplaneService;
import by.piskunou.solvdlaba.service.AirportService;
import by.piskunou.solvdlaba.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository repository;
    private final AirportService airportService;
    private final AirlineService airlineService;
    private final AirplaneService airplaneService;

    @Override
    public List<Flight> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Flight findById(long id) {
       return repository.findById(id)
                        .orElseThrow(() -> new ResourceNotExistsException("There's no flight with such id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> flightSeats(long id, Boolean free) {
        if(free == null) {
            return repository.flightSeats(id);
        }
        if(free.booleanValue()) {
            return repository.flightFreeSeats(id);
        }
        return repository.flightOccupiedSeats(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponse> search(FlightRequest flightRequest) {
        List<Passenger> passengers = flightRequest.getPassengers();

        List<Flight> toFlights = repository.search(
                flightRequest.getFromAirports(),
                flightRequest.getToAirports(),
                passengers.size(),
                flightRequest.getDepartureDate().atStartOfDay(),
                flightRequest.getDepartureDate().atTime(LocalTime.MAX));
        List<BigDecimal> toPrices = cost(passengers, toFlights);

        List<Flight> backFlights = null;
        List<BigDecimal> backPrices = null;

        if(flightRequest.getArrivalDate() != null) {
            backFlights = repository.search(
                    flightRequest.getToAirports(),
                    flightRequest.getFromAirports(),
                    passengers.size(),
                    flightRequest.getArrivalDate().atStartOfDay(),
                    flightRequest.getArrivalDate().atTime(LocalTime.MAX));
            backPrices = cost(passengers, backFlights);
        }
        return buildFlightResponse(toFlights, toPrices, backFlights, backPrices);
    }

    private List<BigDecimal> cost(List<Passenger> passengers, List<Flight> flights) {
        List<BigDecimal> prices = new ArrayList<>();
        for(Flight flight: flights) {
            double flightPrice = flight.getPrice().doubleValue();
            double totalPrice = 0;
            for(Passenger passenger: passengers) {
                if(passenger.getAge() == Passenger.Age.CHILD) {
                    totalPrice += flightPrice * 0.2;
                } else if (passenger.getAge() == Passenger.Age.INFANT) {
                    totalPrice += flightPrice * 0.4;
                }
                totalPrice += flightPrice;
            }
            prices.add(new BigDecimal(totalPrice));
        }
        return prices;
    }

    private List<FlightResponse> buildFlightResponse(List<Flight> toFlights, List<BigDecimal> toPrices,
                                                     List<Flight> backFlights, List<BigDecimal> backPrices) {
        List<FlightResponse> responses = new ArrayList<>();
        if(backFlights == null) {
            for(int i = 0; i < toFlights.size(); i++) {
                FlightResponse response = new FlightResponse();

                response.setToFlight(toFlights.get(i));
                response.setBackFlight(null);
                response.setPrice(toPrices.get(i));

                responses.add(response);
            }
            return responses;
        }

        for(int i = 0; i < toFlights.size(); i++) {
            for(int j = 0; j < backFlights.size(); j++) {
                FlightResponse response = new FlightResponse();

                response.setToFlight(toFlights.get(i));
                response.setBackFlight(backFlights.get(j));
                response.setPrice(toPrices.get(i).add(backPrices.get(j)));

                responses.add(response);
            }
        }
        return responses;
    }

    @Override
    @Transactional
    public Flight create(Flight flight) {
        if(!airportService.isExists(flight.getFrom().getId())) {
            throw new ResourceNotExistsException("There is no origin airport with such id");
        }
        if(!airportService.isExists(flight.getTo().getId())) {
            throw new ResourceNotExistsException("There is no destination airport with such id");
        }
        if(!airlineService.isExists(flight.getAirline().getId())) {
            throw new ResourceNotExistsException("There is no airline with such id");
        }
        if(!airplaneService.isExists(flight.getAirplane().getId())) {
            throw new ResourceNotExistsException("There is no airplane with such id");
        }

        Airplane airplane = airplaneService.findById(flight.getAirplane().getId());
        flight.setSeats( createSeats(airplane) );

        repository.create(flight);
        return flight;
    }

    private List<Seat> createSeats(Airplane airplane) {
        byte seatInRow = airplane.getSeatsInRow();
        short rowNo = airplane.getRowNo();

        List<Seat> seats = new ArrayList<>(seatInRow * rowNo);

        for(short i = 1; i <= rowNo; i++) {
            for(byte j = 1; j <= seatInRow; j++) {
                char c = (char)(64 + j);
                Seat seat = new Seat((i - 1) * seatInRow + j - 1,Short.toString(i) + c);
                seats.add(seat);
            }
        }
        return seats;
    }

    @Override
    @Transactional
    public Flight updateById(long id, Flight flight) {
        if(!isExists(id)) {
            throw new ResourceNotExistsException("There is no flight with such id");
        }
        flight.setId(id);
        if(!airlineService.isExists(flight.getAirline().getId())) {
            throw new ResourceNotExistsException("There is no airline with such id");
        }
        repository.update(flight);
        return flight;
    }

    @Override
    @Transactional
    public void bookSeat(long id, int number) {
        repository.bookSeat(id, number);
    }

    @Override
    @Transactional
    public void removeById(long id) {
        repository.removeById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExists(long id) {
        return repository.isExistsById(id);
    }

}
