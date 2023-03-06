package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.service.CityService;
import by.piskunou.solvdlaba.web.dto.CityDTO;
import by.piskunou.solvdlaba.web.mapper.CityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
@Tag(name = "Cities", description = "Method for work with cities")
public class CityController {

    private final CityService service;
    private final CityMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Information about all cities")
    public List<CityDTO> findAll() {
        return mapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Information about certain city by its id")
    @Parameters({
            @Parameter(name = "id", description = "City's unique identification number"),
            @Parameter(name = "withAirports", description = "Flag to return city with list of airports or no")
    })
    public CityDTO findById(@PathVariable long id, @RequestParam(required = false) boolean withAirports) {
        return mapper.toDTO( service.findById(id, withAirports) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for city(-ies)")
    @Parameter(name = "inquiry", description = "Search cities with name like in this inquiry", example = "Mi")
    public List<CityDTO> search(@RequestParam String inquiry) {
        return mapper.toDTO( service.search(inquiry) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create city")
    @Parameters({
            @Parameter(name = "countryId", description = "The country's unique id which this city belongs to"),
            @Parameter(name = "dto", description = "Created city")
    })
    public CityDTO create(@RequestParam("country-id") long countryId,
                          @RequestBody @Validated CityDTO dto) {
        City city = mapper.toEntity(dto);
        return mapper.toDTO( service.create(countryId, city) );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update city by its id")
    @Parameters({
            @Parameter(name = "id", description = "The unique city's identification number"),
            @Parameter(name = "countryId", description = "The country's unique id which this city belongs to"),
            @Parameter(name = "dto", description = "Updated city")
    })
    public CityDTO updateById(@PathVariable long id, @RequestParam("country-id") long countryId,
                              @RequestBody @Validated CityDTO dto) {
        City city = mapper.toEntity(dto);
        return mapper.toDTO( service.updateById(id, countryId, city) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Remove city from system by its id")
    @Parameter(name = "id", description = "The unique city's identification number")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

}
