package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.web.dto.AirportDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    AirportDTO toDTO(Airport entity);

    Airport toEntity(AirportDTO dto);

    List<AirportDTO> toDTO(List<Airport> entities);

}
