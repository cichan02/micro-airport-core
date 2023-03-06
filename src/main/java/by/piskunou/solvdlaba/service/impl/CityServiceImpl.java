package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.CityRepository;
import by.piskunou.solvdlaba.service.CityService;
import by.piskunou.solvdlaba.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository repository;
    private final CountryService countryService;

    @Override
    @Transactional(readOnly = true)
    public List<City> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public City findById(long id, boolean withAirports) {
        Optional<City> city = withAirports ? repository.findByIdWithAirports(id) : repository.findById(id);
        return city.orElseThrow(() -> new ResourceNotExistsException("There's no city with such id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> search(String inquiry) {
        return repository.search('%' + inquiry + '%');
    }

    @Override
    @Transactional
    public City create(long countryId, City city) {
        checkIsEntityValid(countryId, city);
        repository.create(countryId, city);
        return city;
    }

    @Override
    @Transactional
    public City updateById(long id, long countryId, City city) {
        if(!isExists(id)) {
            return create(countryId, city);
        }
        city.setId(id);
        checkIsEntityValid(countryId, city);
        repository.update(countryId, city);
        return city;
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
    public boolean isExists(Long id, String name) {
        return repository.isExistsByName(id, name);
    }

    private void checkIsEntityValid(long countryId, City city) {
        if(!countryService.isExists(countryId)) {
            throw new ResourceNotExistsException("There's no country with such id");
        }
        if(isExists(city.getId(), city.getName())) {
            throw new ResourceAlreadyExistsException("City with such name has already exists");
        }
    }

}
