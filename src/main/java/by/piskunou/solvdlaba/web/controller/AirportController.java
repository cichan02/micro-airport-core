package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.service.AirportService;
import by.piskunou.solvdlaba.web.dto.AirportDTO;
import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import by.piskunou.solvdlaba.web.dto.groups.onSearch;
import by.piskunou.solvdlaba.web.mapper.AirportMapper;
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
@RequestMapping("/airports")
@RequiredArgsConstructor
@Tag(name = "Airports", description = "Admin's methods for work with airports")
public class AirportController {

    private final AirportService service;
    private final AirportMapper mapper;

    @GetMapping
    @Operation(summary = "Information about all airports")
    public List<AirportDTO> findAll() {
        return mapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Full information about certain airport by id")
    @Parameter(name = "id", description = "The airport's unique identification number")
    public AirportDTO findById(@PathVariable long id) {
        return mapper.toDTO( service.findById(id) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for airports")
    @Parameter(name = "dto", description = "Search airports with fields like in this dto")
    public List<AirportDTO> search(@Validated(onSearch.class) AirportDTO dto) {
        Airport airport = mapper.toEntity(dto);
        return mapper.toDTO( service.search(airport) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create airport")
    @Parameters({
            @Parameter(name = "cityId", description = "The unique city's id to which this airport belongs"),
            @Parameter(name = "dto", description = "Created airport")
    })
    public AirportDTO create(@RequestParam("city-id") long cityId,
                             @RequestBody @Validated(onCreate.class) AirportDTO dto) {
        Airport airport = mapper.toEntity(dto);
        return mapper.toDTO( service.create(cityId, airport) );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update airport by id")
    @Parameters({
            @Parameter(name = "id", description = "Airport's unique identification number"),
            @Parameter(name = "cityId", description = "City's unique identification number"),
            @Parameter(name = "dto", description = "Updated airport")
    })
    public AirportDTO updateById(@PathVariable long id, @RequestParam("city-id") long cityId,
                                 @RequestBody @Validated(onCreate.class) AirportDTO dto) {
        Airport airport = mapper.toEntity(dto);
        return mapper.toDTO( service.updateById(id, cityId, airport) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove airport by its id")
    @Parameter(name = "id", description = "Airport's unique identification number")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

}
