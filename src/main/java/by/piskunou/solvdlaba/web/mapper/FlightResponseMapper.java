package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.flight.FlightResponse;
import by.piskunou.solvdlaba.web.dto.flight.FlightResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightResponseMapper {

    FlightResponseDTO toDTO(FlightResponse entity);

    List<FlightResponseDTO> toDTO(List<FlightResponse> entities);

}
