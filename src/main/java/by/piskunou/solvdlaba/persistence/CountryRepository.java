package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CountryRepository {

    List<Country> findAll();

    Optional<Country> findById(long id);

    void create(Country country);

    void update(Country country);

    void removeById(long id);

    boolean isExistsById(long id);

    boolean isExistsByName(@Param("id") Long id, @Param("name") String name);

}
