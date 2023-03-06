package by.piskunou.solvdlaba.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication required info")
public class AuthEntityDTO {

    @Schema(description = "Jwt access token")
    @NotNull(message = "Access token should be not null")
    private String accessToken;

    @Schema(description = "Jwt refresh token")
    @NotNull(message = "Refresh token should be not null")
    private String refreshToken;

}
