package by.piskunou.solvdlaba.web.dto;

import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import by.piskunou.solvdlaba.web.dto.groups.onSearch;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Airline info")
public class AirportDTO {

    @Schema(description = "The airport's unique identification id")
    @Null(groups = {onCreate.class, onSearch.class}, message = "Id should be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @Schema(description = "Full airport's name")
    @NotBlank(groups = onCreate.class, message = "Airport name should be not blank")
    @Size(max = 200, groups = onCreate.class, message = "Airport name should be less than 200 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @Schema(description = "The airport's unique IATA(International Air Transport Association) code")
    @Size(max = 3, groups = onSearch.class, message = "IATA code should be less than 3 characters")
    @Size(min = 3, max = 3, groups = onCreate.class, message = "IATA code length should be 3")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String iata;

    @Schema(description = "The airport's unique ICAO(International Civil Aviation Organization) code")
    @Size(max = 4, groups = onSearch.class, message = "IATA code should be less than 4 characters")
    @Size(min = 4, max = 4, groups = onCreate.class, message = "ICAO code length should be 4")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String icao;

}
