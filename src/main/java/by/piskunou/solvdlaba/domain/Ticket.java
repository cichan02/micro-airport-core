package by.piskunou.solvdlaba.domain;

import by.piskunou.solvdlaba.domain.flight.Flight;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private Long id;
    private Passenger passenger;
    private Flight flight;
    private Seat seat;

}
