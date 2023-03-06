package by.piskunou.solvdlaba.domain.flight;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {

    public Flight toFlight;
    public Flight backFlight;
    public BigDecimal price;

}
