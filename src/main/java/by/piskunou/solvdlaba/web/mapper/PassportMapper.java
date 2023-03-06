package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.Passport;
import by.piskunou.solvdlaba.web.dto.PassportDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    PassportDTO toDTO(Passport entity);

    Passport toEntity(PassportDTO dto);

}
