package by.piskunou.solvdlaba.domain;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public enum Role {
        ADMIN, USER
    }

    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private List<Ticket> tickets;

    public User(String username) {
        this.username = username;
    }

    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
