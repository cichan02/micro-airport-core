package by.piskunou.solvdlaba.persistence;

import by.piskunou.solvdlaba.domain.Passport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PassportRepository {

    void create(Passport passport);

}
