package by.piskunou.solvdlaba.web.mapper.impl;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.domain.flight.FlightRequest;
import by.piskunou.solvdlaba.web.dto.flight.FlightRequestDTO;
import by.piskunou.solvdlaba.web.mapper.FlightRequestMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlightRequestMapperImpl implements FlightRequestMapper {

    @Override
    public FlightRequest toEntity(FlightRequestDTO dto) {
        if(dto == null) {
            return null;
        }

        FlightRequest flightRequest = new FlightRequest();

        String[] passengersAmount = dto.getPassengers()
                                       .split("-");

        flightRequest.setFromAirports( dto.getFromAirports() );
        flightRequest.setToAirports( dto.getToAirports() );
        flightRequest.setPassengers( parsePassengers( passengersAmount ) );
        flightRequest.setDepartureDate( dto.getDepartureDate() );
        flightRequest.setArrivalDate( dto.getArrivalDate() );

        return flightRequest;
    }

    private List<Passenger> parsePassengers(String[] passengersAmount) {
        List<Passenger> passengers = new ArrayList<>( passengersAmount.length );
        byte j = 0;
        for(String n : passengersAmount) {
            for(short i = 0; i < Short.parseShort(n); i++) {
                Passenger passenger = new Passenger( Passenger.Age.values()[j] );
                passengers.add(passenger);
            }
            j++;
        }
        return passengers;
    }

}
