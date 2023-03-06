package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.service.AirlineService;
import by.piskunou.solvdlaba.web.dto.AirlineDTO;
import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import by.piskunou.solvdlaba.web.dto.groups.onSearch;
import by.piskunou.solvdlaba.web.mapper.AirlineMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airlines")
@RequiredArgsConstructor
@Tag(name = "Airlines", description = "Admin's methods for work with airlines")
public class AirlineController {

    private final AirlineService service;
    private final AirlineMapper mapper;

    @GetMapping
    @Operation(summary = "Information about all airlines")
    public List<AirlineDTO> findAll() {
        return mapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Information about certain airlines by id")
    @Parameter(name = "id", description = "The airline unique identification number")
    public AirlineDTO findById(@PathVariable long id) {
        return mapper.toDTO( service.findById(id) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for airlines")
    @Parameter(name = "dto", description = "Search airline(s) with fields like in this dto")
    public List<AirlineDTO> search(@Validated(onSearch.class) AirlineDTO dto) {
        Airline airline = mapper.toEntity(dto);
        return mapper.toDTO( service.search(airline) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create airline")
    @Parameter(name = "dto", description = "Created airline")
    public AirlineDTO create(@RequestBody @Validated(onCreate.class) AirlineDTO dto) {
        Airline airline = mapper.toEntity(dto);
        return mapper.toDTO( service.create(airline) );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update all airline's by id")
    @Parameters({
            @Parameter(name = "id", description = "The airline's unique identification number"),
            @Parameter(name = "dto", description = "Updated airline")
    })
    public AirlineDTO updateById(@PathVariable long id, @RequestBody @Validated(onCreate.class) AirlineDTO dto) {
        Airline airline = mapper.toEntity(dto);
        return mapper.toDTO( service.updateById(id, airline) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove airline by id")
    @Parameter(name = "id", description = "The airline's unique identification number")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

}
