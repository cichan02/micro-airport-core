package by.piskunou.solvdlaba.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    private int number;
    private String place;
    private boolean free;

    public Seat(int number) {
        this.number = number;
    }

    public Seat(String place) {
        this.place = place;
        this.free = true;
    }

    public Seat(int number, String place) {
        this.number = number;
        this.place = place;
        this.free = true;
    }

}
