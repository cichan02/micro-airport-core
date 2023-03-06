package by.piskunou.solvdlaba.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Seat info")
public class SeatDTO {

    @Schema(description = "Seat's number in airplane", example = "1")
    @NotNull(message = "Number should be not null")
    private Integer number;

    @Schema(description = "Place of the seat", example = "1A")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String place;

    @Schema(description = "Flag describe is seat free")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean free;

}
