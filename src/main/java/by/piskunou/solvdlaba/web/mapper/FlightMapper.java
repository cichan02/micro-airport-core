package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.web.dto.flight.FlightDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightDTO toDTO(Flight entity);

    Flight toEntity(FlightDTO dto);

    List<FlightDTO> toDTO(List<Flight> entities);

}
