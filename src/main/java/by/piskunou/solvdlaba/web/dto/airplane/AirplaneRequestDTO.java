package by.piskunou.solvdlaba.web.dto.airplane;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Search request to find certain airplane(s)")
public class AirplaneRequestDTO {

    @Schema(description = "Inquiry of model", example = "A7")
    private String modelInquiry;

    @Schema(description = "Amount of the seats in airplane")
    @Max(value = Short.MAX_VALUE, message = "Amount of seats should be less than 32767")
    private Short seatAmount;

}
