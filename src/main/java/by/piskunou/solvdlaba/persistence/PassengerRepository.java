package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Passenger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PassengerRepository {

    void create(Passenger passenger);

    boolean isExistsById(long id);
    
}
