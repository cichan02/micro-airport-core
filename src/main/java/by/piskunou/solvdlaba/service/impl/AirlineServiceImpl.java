package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirlineRepository;
import by.piskunou.solvdlaba.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Airline> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Airline findById(long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResourceNotExistsException("There's no airline with such id"));
    }

    @Override
    @Transactional
    public List<Airline> search(Airline airline) {
        setSearchValue(airline::getName, airline::setName);
        setSearchValue(airline::getIata, airline::setIata);
        setSearchValue(airline::getIcao, airline::setIcao);
        setSearchValue(airline::getCallsign, airline::setCallsign);
        return repository.search(airline);
    }

    @Override
    @Transactional
    public Airline create(Airline airline) {
        checkIsEntityValid(airline);
        repository.create(airline);
        return airline;
    }

    @Override
    @Transactional
    public Airline updateById(long id, Airline airline) {
        if(!isExists(id)) {
            return create(airline);
        }
        airline.setId(id);
        checkIsEntityValid(airline);
        repository.update(airline);
        return airline;
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

    @Override
    @Transactional(readOnly = true)
    public boolean isExistsByCallsign(Long id, String callsign) {
        return repository.isExistsByCallsign(id, callsign);
    }

    private void setSearchValue(Supplier<String> getter, Consumer<String> setter) {
        if(getter.get() != null) {
            setter.accept('%' + getter.get() + '%');
        } else {
            setter.accept("%%");
        }
    }

    private void checkIsEntityValid(Airline airline) {
        Long id = airline.getId();
        if(isExistsByName(id, airline.getName())) {
            throw new ResourceAlreadyExistsException("Airline with such name has already exists");
        }
        if(isExistsByIata(id, airline.getIata())) {
            throw new ResourceAlreadyExistsException("Airline with such IATA has already exists");
        }
        if(isExistsByIcao(id, airline.getIcao())) {
            throw new ResourceAlreadyExistsException("Airline with such ICAO has already exists");
        }
        if(isExistsByCallsign(id, airline.getCallsign())) {
            throw new ResourceAlreadyExistsException("Airline with such callsign has already exists");
        }
    }

}
