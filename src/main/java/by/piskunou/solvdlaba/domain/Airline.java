package by.piskunou.solvdlaba.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Airline {

    private Long id;

    private String name;
    private String iata;
    private String icao;
    private String callsign;

    public Airline(Long id) {
        this.id = id;
    }

    public Airline(String name) {
        this.name = name;
    }

    public Airline(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Airline(String name, String iata, String icao, String callsign) {
        this.name = name;
        this.iata = iata;
        this.icao = icao;
        this.callsign = callsign;
    }

}
