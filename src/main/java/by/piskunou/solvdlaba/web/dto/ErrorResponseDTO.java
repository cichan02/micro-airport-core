package by.piskunou.solvdlaba.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponseDTO {

    @Schema(description = "Field where error was invoked")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @Schema(description = "Error message")
    private String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

}
