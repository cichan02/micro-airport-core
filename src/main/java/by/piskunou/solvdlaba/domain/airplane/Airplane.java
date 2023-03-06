package by.piskunou.solvdlaba.domain.airplane;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Airplane {

    private Long id;
    private String model;
    private Byte seatsInRow;
    private Short rowNo;

    public Airplane(Long id) {
        this.id = id;
    }

    public Airplane(Long id, String model) {
        this.id = id;
        this.model = model;
    }

}
