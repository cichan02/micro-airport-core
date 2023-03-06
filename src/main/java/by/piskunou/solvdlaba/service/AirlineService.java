package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airline;

import java.util.List;

public interface AirlineService {

    List<Airline> findAll();

    Airline findById(long id);

    List<Airline> search(Airline airline);

    Airline create(Airline airline);

    Airline updateById(long id, Airline airline);

    void removeById(long id);

    boolean isExists(long id);

    boolean isExistsByName(Long id, String name);

    boolean isExistsByIata(Long id, String iata);

    boolean isExistsByIcao(Long id, String icao);

    boolean isExistsByCallsign(Long id, String callsign);

}
