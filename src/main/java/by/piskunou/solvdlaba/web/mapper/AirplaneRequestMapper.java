package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import by.piskunou.solvdlaba.web.dto.airplane.AirplaneRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirplaneRequestMapper {

    @Mapping(target = "modelInquiry", defaultValue = "")
    AirplaneRequest toEntity(AirplaneRequestDTO dto);

}
