package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;

import java.util.List;

public interface AirplaneService {

    List<Airplane> findAll();

    Airplane findById(long id);

    List<Airplane> search(AirplaneRequest request);

    Airplane create(Airplane airplane);

    Airplane updateById(long id, Airplane airplane);

    void removeById(long id);

    boolean isExists(long id);

    boolean isExists(Long id, String model);

}
