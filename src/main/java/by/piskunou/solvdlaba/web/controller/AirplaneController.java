package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import by.piskunou.solvdlaba.service.AirplaneService;
import by.piskunou.solvdlaba.web.dto.airplane.AirplaneDTO;
import by.piskunou.solvdlaba.web.dto.airplane.AirplaneRequestDTO;
import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import by.piskunou.solvdlaba.web.mapper.AirplaneMapper;
import by.piskunou.solvdlaba.web.mapper.AirplaneRequestMapper;
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
@RequestMapping("/airplanes")
@RequiredArgsConstructor
@Tag(name = "Airplanes", description = "Admin's methods fro work with airplanes")
public class AirplaneController {

    private final AirplaneService service;
    private final AirplaneMapper airplaneMapper;
    private final AirplaneRequestMapper requestMapper;

    @GetMapping
    @Operation(summary = "Information about all airplanes")
    public List<AirplaneDTO> findAll() {
        return airplaneMapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Full information about certain airplane by its id")
    @Parameter(name = "id", description = "The airplane's unique identification number")
    public AirplaneDTO findById(@PathVariable int id) {
        return airplaneMapper.toDTO( service.findById(id) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for airplanes")
    @Parameter(name = "requestDTO", description = "Search airplanes with parameters like in this request")
    public List<AirplaneDTO> search(@Validated AirplaneRequestDTO requestDTO) {
        AirplaneRequest request = requestMapper.toEntity(requestDTO);
        return airplaneMapper.toDTO( service.search(request) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create airplane")
    @Parameter(name = "dto", description = "Created airplane")
    public AirplaneDTO create(@RequestBody @Validated(onCreate.class) AirplaneDTO dto) {
        Airplane airplane = airplaneMapper.toEntity(dto);
        return airplaneMapper.toDTO( service.create(airplane) );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update airplane by id")
    @Parameters({
            @Parameter(name = "id", description = "The airplanes unique identification number"),
            @Parameter(name = "dto", description = "Updated airplane")
    })
    public AirplaneDTO updateById(@PathVariable long id, @RequestBody @Validated(onCreate.class) AirplaneDTO dto) {
        Airplane airplane = airplaneMapper.toEntity(dto);
        return airplaneMapper.toDTO( service.updateById(id, airplane) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove airplane by id")
    @Parameter(name = "id", description = "The airplanes unique identification number")
    public void removeById(@PathVariable int id) {
        service.removeById(id);
    }

}
