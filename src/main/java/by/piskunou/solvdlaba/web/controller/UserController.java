package by.piskunou.solvdlaba.web.controller;

import by.piskunou.solvdlaba.domain.User;
import by.piskunou.solvdlaba.service.UserService;
import by.piskunou.solvdlaba.web.dto.PasswordDTO;
import by.piskunou.solvdlaba.web.dto.UserDTO;
import by.piskunou.solvdlaba.web.dto.groups.onSearch;
import by.piskunou.solvdlaba.web.dto.groups.onUpdate;
import by.piskunou.solvdlaba.web.mapper.PasswordMapper;
import by.piskunou.solvdlaba.web.mapper.UserMapper;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Methods for work with users")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;
    private final PasswordMapper passwordMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Information about all users")
    public List<UserDTO> findAll() {
        return userMapper.toDTO( service.findAll() );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasUser(#id) or hasAuthority('ADMIN')")
    @Operation(summary = "Full user's information by id")
    @Parameters({
            @Parameter (name = "id", description = "User's id"),
            @Parameter (name = "withTickets", description = "Flag to get user with tickets or no")
    })
    public UserDTO findById(@PathVariable long id, @RequestParam(required = false) boolean withTickets) {
        return userMapper.toDTO( service.findById(id, withTickets) );
    }

    @GetMapping("/search")
    @Operation(summary = "Search for users")
    @Parameter(name = "dto", description = "Search user(s) with field like in this dto")
    public List<UserDTO> search(@Validated(onSearch.class) UserDTO dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDTO( service.search(user) );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasUser(#id) or hasAuthority('ADMIN')")
    @Operation(summary = "Update all user's fields by id")
    @Parameters({
            @Parameter(name = "id", description = "User identification number"),
            @Parameter(name = "dto", description = "Updated user")
    })
    public UserDTO updateById(@PathVariable long id, @RequestBody @Validated(onUpdate.class) UserDTO dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toDTO( service.updateById(id, user) );
    }

    @PatchMapping("/{id}/password/update")
    @PreAuthorize("hasUser(#id)")
    @Operation(summary = "Update password by id")
    @Parameters({
            @Parameter(name = "id", description = "User identification number"),
            @Parameter(name = "dto", description = "Updated password")
    })
    public void updatePasswordById(@PathVariable long id, @RequestBody @Validated(onUpdate.class) PasswordDTO dto) {
        service.updatePasswordById(id, passwordMapper.toEntity(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasUser(#id) or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove by id")
    @Parameter(name = "id", description = "User identification number")
    public void removeById(@PathVariable int id) {
        service.removeById(id);
    }

}
