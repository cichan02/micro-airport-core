package by.piskunou.solvdlaba.web.dto;

import by.piskunou.solvdlaba.web.dto.groups.onUpdate;
import by.piskunou.solvdlaba.web.validation.annotation.FieldsValueMatch;
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
@Schema(description = "Password info")
@FieldsValueMatch(
        groups = {onUpdate.class, PasswordDTO.onEdit.class},
        field = "newPassword",
        fieldMatch = "confirmPassword",
        message = "Password confirmation doesn't match password"
)
public class PasswordDTO {

    public interface onEdit {

    }

    @Schema(description = "Old user's password")
    @Null(groups = onEdit.class, message = "Old password should be null")
    @NotBlank(groups = onUpdate.class, message = "Password shouldn't be blank")
    @Size(groups = onUpdate.class, min = 8, max = 300,
            message = "Password should be between 8 and 300 characters")
    private String oldPassword;

    @Schema(description = "New user's password")
    @NotBlank(groups = {onUpdate.class, onEdit.class}, message = "Password shouldn't be blank")
    @Size(groups = {onUpdate.class, onEdit.class}, min = 8, max = 300,
            message = "Password should be between 8 and 300 characters")
    private String newPassword;

    @Schema(description = "Confirmed new user's password")
    private String confirmPassword;

}
