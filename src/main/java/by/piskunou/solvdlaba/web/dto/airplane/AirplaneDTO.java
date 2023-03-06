package by.piskunou.solvdlaba.web.dto.airplane;

import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Airplane ifo")
public class AirplaneDTO {

    @Schema(description = "Airplane's unique identification number")
    @Null(groups = onCreate.class, message = "Id should be null")
    private Long id;

    @Schema(description = "Airplane's model", example = "Boeing 767")
    @NotBlank(groups = onCreate.class, message = "Model name should be not blank")
    @Size(groups = onCreate.class, max = 300, message = "Model name should be less than 300 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String model;

    @Schema(description = "Amount of the seats in row in this airplane's model")
    @NotNull(groups = onCreate.class, message = "Amount of seats in row should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Byte seatsInRow;

    @Schema(description = "Amount of the rows in this airplane's model")
    @NotNull(groups = onCreate.class, message = "Amount of rows should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Short rowNo;

}
