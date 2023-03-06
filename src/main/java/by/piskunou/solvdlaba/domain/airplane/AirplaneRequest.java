package by.piskunou.solvdlaba.domain.airplane;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneRequest {

    private String modelInquiry;
    private short seatAmount;

}
