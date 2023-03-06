package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.AuthEntity;
import by.piskunou.solvdlaba.domain.Password;
import by.piskunou.solvdlaba.domain.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface AuthService {

    AuthEntity signUp(User user);

    AuthEntity login(User user);

    AuthEntity refresh(AuthEntity authEntity);

    void createPassword(String email) throws TemplateException, MessagingException, IOException;

    void editPassword(String token, Password password);

}
