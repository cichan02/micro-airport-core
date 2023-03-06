package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Airport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AirportRepository {

    List<Airport> findAll();

    Optional<Airport> findById(long id);

    List<Airport> search(Airport airport);

    void create(@Param("cityId") long cityId, @Param("airport") Airport airport);

    void update(@Param("cityId") long cityId, @Param("airport") Airport airport);

    void removeById(long id);

    boolean isExistsById(long id);

    boolean isExistsByName(@Param("id") Long id, @Param("name") String name);

    boolean isExistsByIata(@Param("id") Long id, @Param("iata") String iata);

    boolean isExistsByIcao(@Param("id") Long id, @Param("icao") String icao);

}
