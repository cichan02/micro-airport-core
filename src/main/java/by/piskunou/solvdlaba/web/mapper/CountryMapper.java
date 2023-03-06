package by.piskunou.solvdlaba.web.mapper;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.web.dto.CountryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDTO toDTO(Country entity);

    Country toEntity(CountryDTO dto);

    List<CountryDTO> toDTO(List<Country> countries);

}
