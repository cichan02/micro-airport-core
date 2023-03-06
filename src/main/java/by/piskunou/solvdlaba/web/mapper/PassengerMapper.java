package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.Passenger;
import by.piskunou.solvdlaba.web.dto.PassengerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerDTO toDTO(Passenger entity);

    Passenger toEntity(PassengerDTO dto);

}
