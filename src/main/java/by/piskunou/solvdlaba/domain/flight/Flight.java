package by.piskunou.solvdlaba.domain.flight;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.Seat;
import by.piskunou.solvdlaba.domain.airplane.Airplane;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    private Long id;
    private Airport from;
    private Airport to;
    private Airplane airplane;
    private Airline airline;
    private BigDecimal price;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private List<Seat> seats;

    public Flight(Long id) {
        this.id = id;
    }

    public Flight(long id, LocalDateTime departureTime) {
        this.id = id;
        this.departureTime = departureTime;
    }

    public Flight(long id, BigDecimal price, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.id = id;
        this.price = price;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

}
