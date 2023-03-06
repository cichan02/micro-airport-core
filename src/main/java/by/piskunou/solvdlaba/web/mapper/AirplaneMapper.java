package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.web.dto.airplane.AirplaneDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirplaneMapper {

    AirplaneDTO toDTO(Airplane entity);

    Airplane toEntity(AirplaneDTO dto);

    List<AirplaneDTO> toDTO(List<Airplane> entities);

}
