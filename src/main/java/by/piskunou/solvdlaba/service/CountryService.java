package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Country;

import java.util.List;

public interface CountryService {

    List<Country> findAll();

    Country findById(long id);

    Country create(Country country);

    Country updateById(long id, Country country);

    void removeById(long id);

    boolean isExists(long id);

    boolean isExists(Long id, String name);

}
