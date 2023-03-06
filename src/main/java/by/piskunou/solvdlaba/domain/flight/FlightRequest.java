package by.piskunou.solvdlaba.domain.flight;

import by.piskunou.solvdlaba.domain.Passenger;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {

    private List<String> fromAirports;
    private List<String> toAirports;
    private List<Passenger> passengers;
    private LocalDate departureDate;
    private LocalDate arrivalDate;

}
