package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirplaneRepository;
import by.piskunou.solvdlaba.service.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Airplane> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Airplane findById(long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResourceNotExistsException("There's no airplane with such id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Airplane> search(AirplaneRequest request) {
        request.setModelInquiry('%' + request.getModelInquiry() + '%');
        return repository.search(request);
    }

    @Override
    @Transactional
    public Airplane create(Airplane airplane) {
        checkIsEntityValid(airplane);
        repository.create(airplane);
        return airplane;
    }

    @Override
    @Transactional
    public Airplane updateById(long id, Airplane airplane) {
        if(!isExists(id)) {
            return create(airplane);
        }
        airplane.setId(id);
        checkIsEntityValid(airplane);
        repository.update(airplane);
        return airplane;
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
    public boolean isExists(Long id, String model) {
        return repository.isExistsByModel(id, model);
    }

    private void checkIsEntityValid(Airplane airplane) {
        if(isExists(airplane.getId(), airplane.getModel())) {
            throw new ResourceAlreadyExistsException("Such model of airplane already exists");
        }
    }

}
