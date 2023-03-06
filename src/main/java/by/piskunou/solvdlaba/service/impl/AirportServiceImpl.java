package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirportRepository;
import by.piskunou.solvdlaba.service.AirportService;
import by.piskunou.solvdlaba.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final CityService cityService;
    private final AirportRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Airport> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Airport findById(long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResourceNotExistsException("There's no airplane with such id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Airport> search(Airport airport) {
        setSearchValue(airport::getName, airport::setName);
        setSearchValue(airport::getIata, airport::setIata);
        setSearchValue(airport::getIcao, airport::setIcao);
        return repository.search(airport);
    }

    @Override
    @Transactional
    public Airport create(long cityId, Airport airport) {
        checkIsEntityValid(cityId, airport);
        repository.create(cityId, airport);
        return airport;
    }

    @Override
    @Transactional
    public Airport updateById(long id, long cityId, Airport airport) {
        if(!isExists(id)) {
            return create(cityId, airport);
        }
        airport.setId(id);
        checkIsEntityValid(cityId, airport);
        repository.update(cityId, airport);
        return airport;
    }


    @Override
    @Transactional
    public void removeById(long id) {
        repository.removeById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExists(long id) {
        return repository.isExistsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByName(Long id, String name) {
        return repository.isExistsByName(id, name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByIata(Long id, String iata) {
        return repository.isExistsByIata(id, iata);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByIcao(Long id, String icao) {
        return repository.isExistsByIcao(id, icao);
    }

    private void setSearchValue(Supplier<String> getter, Consumer<String> setter) {
        if(getter.get() != null) {
            setter.accept('%' + getter.get() + '%');
        } else {
            setter.accept("%%");
        }
    }

    private void checkIsEntityValid(long cityId, Airport airport) {
        if(!cityService.isExists(cityId)) {
            throw new ResourceNotExistsException("There's is no city with such id");
        }
        Long id = airport.getId();
        if(isExistsByName(id, airport.getName())) {
            throw new ResourceAlreadyExistsException("Airport with such name has already exists");
        }
        if(isExistsByIata(id, airport.getIata())) {
            throw new ResourceAlreadyExistsException("Airport with such IATA has already exists");
        }
        if(isExistsByIcao(id, airport.getIcao())) {
            throw new ResourceAlreadyExistsException("Airport with such ICAO has already exists");
        }
    }

}
