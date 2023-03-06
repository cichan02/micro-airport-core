package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.City;

import java.util.List;

public interface CityService {

    List<City> findAll();

    City findById(long id, boolean withAirports);

    List<City> search(String inquiry);

    City create(long countryId, City city);

    City updateById(long id, long countryId, City city);

    void removeById(long id);

    boolean isExists(long id);

    boolean isExists(Long id, String name);

}
