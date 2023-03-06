package by.piskunou.solvdlaba.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public interface EmailService {

    void sendMessage(String email, Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException;

}
