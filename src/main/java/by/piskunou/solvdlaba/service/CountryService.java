package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Country;

public interface CountryService {

    Country findById(long id);

    boolean isExists(long id);

}
