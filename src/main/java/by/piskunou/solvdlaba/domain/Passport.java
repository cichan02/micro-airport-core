package by.piskunou.solvdlaba.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Passport {

    private Long id;
    private String number;
    private String identificationNo;

}
