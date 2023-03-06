package by.piskunou.solvdlaba.web.dto.flight;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Search response.")
public class FlightResponseDTO {

    @Schema(description = "To flight")
    public FlightDTO toFlight;

    @Schema(description = "Back flight")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public FlightDTO backFlight;

    @Schema(description = "Certain price, which depends from list of passengers, to & back flight")
    public BigDecimal price;

}
