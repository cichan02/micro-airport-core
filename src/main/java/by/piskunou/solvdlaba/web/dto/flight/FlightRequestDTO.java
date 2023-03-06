package by.piskunou.solvdlaba.web.dto.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Search request to find certain flight(s)")
public class FlightRequestDTO {

    @Schema(description = "IATA or ICAO codes of origin airports")
    @NotNull(message = "Please, select origin")
    private List<String> fromAirports;

    @Schema(description = "IATA or ICAO codes of destination airports")
    @NotNull(message = "Please, select destination")
    private List<String> toAirports;

    @Schema(description = "String of passenger's ages")
    @NotNull(message = "Passengers should be not null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "\\d-\\d-\\d")
    private String passengers;

    @Schema(description = "Departure date")
    @NotNull(message = "Departure time should be not null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "Departure date should be future")
    private LocalDate departureDate;

    @Schema(description = "Return date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Future(message = "Return date should be future")
    private LocalDate arrivalDate;

}
