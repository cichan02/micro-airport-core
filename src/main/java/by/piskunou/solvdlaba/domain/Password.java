package by.piskunou.solvdlaba.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Password {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
