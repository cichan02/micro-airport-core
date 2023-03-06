package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.flight.Flight;
import by.piskunou.solvdlaba.domain.flight.FlightRequest;
import by.piskunou.solvdlaba.service.FlightService;
import by.piskunou.solvdlaba.web.dto.SeatDTO;
import by.piskunou.solvdlaba.web.dto.flight.FlightDTO;
import by.piskunou.solvdlaba.web.dto.flight.FlightRequestDTO;
import by.piskunou.solvdlaba.web.dto.flight.FlightResponseDTO;
import by.piskunou.solvdlaba.web.dto.groups.onCreate;
import by.piskunou.solvdlaba.web.dto.groups.onUpdate;
import by.piskunou.solvdlaba.web.mapper.FlightMapper;
import by.piskunou.solvdlaba.web.mapper.FlightRequestMapper;
import by.piskunou.solvdlaba.web.mapper.FlightResponseMapper;
import by.piskunou.solvdlaba.web.mapper.SeatMapper;
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
@RequestMapping("/flights")
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Methods for work with flights")
public class FlightController {

    private final FlightService service;
    private final FlightMapper flightMapper;
    private final FlightRequestMapper flightRequestMapper;
    private final FlightResponseMapper flightResponseMapper;
    private final SeatMapper seatMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Information about all flights")
    public List<FlightDTO> findAll(){
        return flightMapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Full information about certain flight by its id")
    @Parameter(name = "id", description = "The flight's unique identification number")
    public FlightDTO findById(@PathVariable long id) {
        return flightMapper.toDTO( service.findById(id) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for flights")
    @Parameter(name = "requestDTO", description = "Search for flights with parameters like in this request")
    public List<FlightResponseDTO> search(@Validated FlightRequestDTO requestDTO) {
        FlightRequest flightRequest = flightRequestMapper.toEntity(requestDTO);
        return flightResponseMapper.toDTO( service.search(flightRequest));
    }

    @GetMapping("/{id}/seats")
    @Operation(summary = "Information about flight's seats")
    @Parameters({
            @Parameter(name = "id", description = "The flight's unique identification number"),
            @Parameter(name = "free", description = "Flag describes which seats should be returned:" +
                    "free(if 'true'), occupied (if 'false') or both (if 'null')")
    })
    public List<SeatDTO> flightSeats(@PathVariable long id, @RequestParam(required = false) Boolean free) {
        return seatMapper.toDTO( service.flightSeats(id, free) );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create flight")
    @Parameter(name = "dto", description = "Created flight")
    public FlightDTO create(@RequestBody @Validated(onCreate.class) FlightDTO dto) {
        Flight flight = flightMapper.toEntity(dto);
        return flightMapper.toDTO( service.create(flight) );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update flight by its id")
    @Parameters({
            @Parameter(name = "id", description = "The flight's unique identification number"),
            @Parameter(name = "dto", description = "Updated flight")
    })
    public FlightDTO updateById(@PathVariable long id, @RequestBody @Validated(onUpdate.class) FlightDTO dto) {
        Flight flight = flightMapper.toEntity(dto);
        return flightMapper.toDTO( service.updateById(id, flight) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Remove flight by its id")
    @Parameter(name = "id", description = "The flight's unique identification number")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }

}
