package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Airline;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AirlineRepository {

    List<Airline> findAll();

    Optional<Airline> findById(long id);

    List<Airline> search(Airline airline);

    void create(Airline airline);

    void update(Airline airline);

    void removeById(long id);

    boolean isExistsById(long id);

    boolean isExistsByName(@Param("id") Long id, @Param("name") String name);

    boolean isExistsByIata(@Param("id") Long id, @Param("iata") String iata);

    boolean isExistsByIcao(@Param("id") Long id, @Param("icao") String icao);

    boolean isExistsByCallsign(@Param("id") Long id, @Param("callsign") String callsign);

}
