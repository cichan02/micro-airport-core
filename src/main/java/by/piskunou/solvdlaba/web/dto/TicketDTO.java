package by.piskunou.solvdlaba.web.dto;

import by.piskunou.solvdlaba.web.dto.flight.FlightDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Ticket info")
public class TicketDTO {

    @Schema(description = "The ticket's unique identification id")
    @Null(message = "Id should be null")
    private Long id;

    @Schema(description = "Passenger which ticket belongs to")
    @NotNull(message = "Passenger should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PassengerDTO passenger;

    @Schema(description = "Flight which ticket belongs to")
    @NotNull(message = "Flight should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FlightDTO flight;

    @Schema(description = "Ticket's seat")
    @NotNull(message = "Seat should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SeatDTO seat;

}
