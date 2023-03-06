package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.Password;
import by.piskunou.solvdlaba.web.dto.PasswordDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordMapper {

    PasswordDTO toDTO(Password entity);

    Password toEntity(PasswordDTO dto);

}
