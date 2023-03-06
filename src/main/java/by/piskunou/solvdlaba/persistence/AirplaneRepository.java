package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AirplaneRepository {

    List<Airplane> findAll();

    Optional<Airplane> findById(long id);

    List<Airplane> search(AirplaneRequest request);

    void create(Airplane airplane);

    void update(Airplane airplane);

    void removeById(long id);

    boolean isExistsById(long id);

    boolean isExistsByModel(@Param("id") Long id, @Param("model") String model);

}
