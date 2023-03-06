package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Password;
import by.piskunou.solvdlaba.domain.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(long id, boolean withTickets);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> search(User user);

    User create(User user);

    User updateById(long id, User user);

    void updatePasswordByUsername(String username, String password);

    void updatePasswordById(long id, Password password);

    void removeById(int id);

    boolean isExists(long id);

    boolean isExistsByUsername(Long id, String username);

    boolean isExistsByEmail(Long id, String email);

    boolean isValidPassword(long id, String password);

}
