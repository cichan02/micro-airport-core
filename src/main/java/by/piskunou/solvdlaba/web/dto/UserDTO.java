package by.piskunou.solvdlaba.web.dto;

import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.web.dto.groups.onSearch;
import by.piskunou.solvdlaba.web.dto.groups.onUpdate;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User info")
public class UserDTO {

    public interface onSignUp {

    }

    public interface onLogin {

    }

    @Schema(description = "The user's unique identification id")
    @Null(groups = {onSignUp.class, onLogin.class, onUpdate.class, onSearch.class}, message = "Id should be null")
    private Long id;

    @Schema(description = "Unique user's name")
    @NotBlank(groups = {onSignUp.class, onLogin.class, onUpdate.class}, message = "Username should be not blank")
    @Size(groups = {onSignUp.class, onLogin.class, onUpdate.class, onSearch.class}, max = 50,
            message = "The username must be less than 50 characters")
    private String username;

    @Schema(description = "User's password", hidden = true)
    @Null(groups = {onSearch.class, onUpdate.class}, message = "Password should be null")
    @NotBlank(groups = {onSignUp.class, onLogin.class}, message = "Password should be not blank")
    @Size(groups = {onSignUp.class, onLogin.class}, min = 8, max = 300, message = "Password should be between 8 and 300 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Schema(description = "Email")
    @Null(groups = {onSearch.class, onLogin.class}, message = "Email should be null")
    @NotBlank(groups = {onSignUp.class, onUpdate.class}, message = "Email shouldn't be blank")
    @Email(groups = {onSignUp.class, onUpdate.class}, message = "String should be valid email")
    @Size(groups = {onSignUp.class, onUpdate.class}, max = 100, message = "Email should be less than 100 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @Schema(description = "User's role")
    @Null(groups = {onSignUp.class, onLogin.class, onSearch.class}, message = "Role should be null")
    @NotNull(groups = onUpdate.class, message = "Role should be not null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User.Role role;

    @Schema(description = "User's tickets")
    @Null(groups = {onSignUp.class, onLogin.class, onUpdate.class, onSearch.class}, message = "Tickets should be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Ticket> tickets;

}
