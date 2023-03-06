package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.Ticket;
import by.piskunou.solvdlaba.service.TicketService;
import by.piskunou.solvdlaba.web.dto.TicketDTO;
import by.piskunou.solvdlaba.web.mapper.TicketMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;
    private final TicketMapper mapper;

    @GetMapping
    @PreAuthorize("hasUser(#userId)")
    @Operation(summary = "Information about all user's ticket")
    @Parameter(name = "userId", description = "Tickets owner's id")
    public List<TicketDTO> findAll(@RequestParam("user-id") long userId) {
        return mapper.toDTO( service.findAll(userId) );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasOwner(#userId, #id)")
    @Operation(summary = "Full information about certain user's ticket by its id")
    @Parameters({
            @Parameter(name = "id", description = "The ticket's unique identification number"),
            @Parameter(name = "userId", description = "The user's unique identification number")
    })
    public TicketDTO findById(@PathVariable("id") long id, @RequestParam("user-id") long userId) {
        return mapper.toDTO( service.findById(id, userId) );
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasUser(#userId)")
    @Operation(summary = "Buy ticket")
    @Parameters({
            @Parameter(name = "userId", description = "The user's unique identification number"),
            @Parameter(name = "dto", description = "Purchased ticket")
    })
    public TicketDTO buyTicket(@RequestParam("user-id") long userId, @RequestBody @Validated TicketDTO dto) {
        Ticket ticket = mapper.toEntity(dto);
        return mapper.toDTO( service.create(userId, ticket) );
    }

}
