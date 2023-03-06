package by.piskunou.solvdlaba.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "City info")
public class CityDTO {

    @Schema(description = "The city's unique identification id")
    @Null(message = "Id should be null")
    private Long id;

    @Schema(description = "City's full name")
    @NotBlank(message = "City name should be not blank")
    @Size(max = 50, message = "City name should be less than50 characters")
    private String name;

    @Schema(description = "Country which city belongs to")
    @Null(message = "Country should be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CountryDTO country;

    @Schema(description = "List of city's airports")
    @Null(message = "List of airports should be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AirportDTO> airports;

}
