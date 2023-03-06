package by.piskunou.solvdlaba.web.dto;

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
@Schema(description = "Country info")
public class CountryDTO {

    @Schema(description = "The country's unique identification id")
    @Null(message = "Id should be null")
    private Long id;

    @Schema(description = "Country's name")
    @NotBlank(message = "Country name should be not blank")
    @Size(max = 50, message = "Country name should be less than 50 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

}
