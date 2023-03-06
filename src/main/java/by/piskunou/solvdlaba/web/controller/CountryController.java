package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.service.CountryService;
import by.piskunou.solvdlaba.web.dto.CountryDTO;
import by.piskunou.solvdlaba.web.mapper.CountryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService service;
    private final CountryMapper mapper;

    @GetMapping
    public List<CountryDTO> findAll() {
        return mapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    public CountryDTO findById(@PathVariable long id) {
        return mapper.toDTO( service.findById(id) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDTO create(@RequestBody @Validated CountryDTO dto) {
        Country country = mapper.toEntity(dto);
        return mapper.toDTO( service.create(country) );
    }

    @PutMapping("/{id}")
    public CountryDTO updateById(@PathVariable long id, @RequestBody @Validated CountryDTO dto) {
        Country country = mapper.toEntity(dto);
        return mapper.toDTO( service.updateById(id, country) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

}
