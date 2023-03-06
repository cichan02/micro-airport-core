package by.piskunou.solvdlaba.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Passport info")
public class PassportDTO {

    @Schema(description = "The passport's unique identification id equals to appropriate passenger's id")
    @Null(message = "Id should be null")
    private Long id;

    @Schema(description = "Passport's number", hidden = true)
    private String number;

    @Schema(description = "Passport;s identification number", hidden = true)
    private String identificationNo;

}
