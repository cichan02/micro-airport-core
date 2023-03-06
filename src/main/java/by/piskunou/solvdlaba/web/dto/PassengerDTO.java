package by.piskunou.solvdlaba.web.dto;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.domain.Passenger;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Passenger info")
public class PassengerDTO {

    @Schema(description = "The passenger's unique identification id")
    @Null(message = "Id should be null")
    private Long id;

    @Schema(description = "Passenger's country")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Country country;

    @Schema(description = "Passenger's first name")
    private String firstname;

    @Schema(description = "Passenger's surname")
    private String surname;

    @Schema(description = "Passenger's passport")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PassportDTO passport;

    @Schema(description = "Passenger's birth date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Schema(description = "Passenger's age category")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Passenger.Age age;

    @Schema(description = "Passenger's gender")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Passenger.Gender gender;

}
