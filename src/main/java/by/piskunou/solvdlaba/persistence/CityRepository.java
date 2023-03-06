package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CityRepository {

    List<City> findAll();

    Optional<City> findById(long id);

    Optional<City> findByIdWithAirports(long id);

    List<City> search(String inquiry);

    void create(@Param("countryId") long countryId, @Param("city") City city);

    void update(@Param("countryId") long countryId, @Param("city") City city);

    void removeById(long id);

    boolean isExistsById(long id);

    boolean isExistsByName(@Param("id") Long id, @Param("name") String name);

}
