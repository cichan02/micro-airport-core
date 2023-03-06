package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airport;

import java.util.List;

public interface AirportService {

    List<Airport> findAll();

    Airport findById(long id);

    List<Airport> search(Airport airport);

    Airport create(long cityId, Airport airport);

    Airport updateById(long id, long cityId, Airport airport);

    void removeById(long id);

    boolean isExists(long id);

    boolean isExistsByName(Long id, String name);

    boolean isExistsByIata(Long id, String iata);

    boolean isExistsByIcao(Long id, String icao);

}
