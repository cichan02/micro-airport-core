package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.AuthEntity;
import by.piskunou.solvdlaba.web.dto.AuthEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthEntityDTO toDTO(AuthEntity entity);

    AuthEntity toEntity(AuthEntityDTO dto);

}
