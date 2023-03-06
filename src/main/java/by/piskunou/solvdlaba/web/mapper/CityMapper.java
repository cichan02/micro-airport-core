package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.web.dto.CityDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDTO toDTO(City entity);

    City toEntity(CityDTO dto);

    List<CityDTO> toDTO(List<City> entities);

}
