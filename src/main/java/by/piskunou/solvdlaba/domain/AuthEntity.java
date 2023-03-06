package by.piskunou.solvdlaba.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity {

    private String accessToken;
    private String refreshToken;

}
