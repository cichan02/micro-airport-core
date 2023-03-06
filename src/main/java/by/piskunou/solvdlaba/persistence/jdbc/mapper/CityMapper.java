package by.piskunou.solvdlaba.persistence.jdbc.mapper;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.City;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityMapper {

    private final AirportMapper airportMapper;

    @SneakyThrows
    public City mapRow(ResultSet rs) {
        return new City(rs.getLong("city_id"),
                        rs.getString("city_name"));
    }

    @SneakyThrows
    public City airportsMapRow(ResultSet rs) {
        City city = mapRow(rs);
        List<Airport> airports = new LinkedList<>();

        rs.previous();
        while(rs.next()) {
            if(city.getId() != rs.getLong("city_id")) {
                break;
            }

            Airport airport = airportMapper.mapRow(rs);
            airports.add(airport);
        }

        city.setAirports(airports);

        return city;
    }

}
