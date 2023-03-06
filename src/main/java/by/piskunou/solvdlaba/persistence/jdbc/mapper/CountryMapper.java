package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryMapper {

    private final CityMapper cityMapper;

    @SneakyThrows
    public Country mapRow(ResultSet rs) {
        return new Country(rs.getLong("country_id"),
                           rs.getString("country_name"));
    }

    @SneakyThrows
    public Country citiesMapRow(ResultSet rs) {
        Country country = mapRow(rs);
        List<City> cities = new LinkedList<>();

        rs.previous();
        while(rs.next()) {
            City city = cityMapper.mapRow(rs);
            cities.add(city);
        }

        return country;
    }

    @SneakyThrows
    public Country airportsMapRow(ResultSet rs) {
        Country country = mapRow(rs);
        List<City> cities = new LinkedList<>();

        rs.previous();
        while(rs.next()) {
            City city = cityMapper.airportsMapRow(rs);
            cities.add(city);

            rs.previous();
        }

        return country;
    }

}
