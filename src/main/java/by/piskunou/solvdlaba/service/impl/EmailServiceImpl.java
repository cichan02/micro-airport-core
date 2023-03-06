package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.service.EmailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final JavaMailSender emailSender;

    @Override
    public void sendMessage(String email, Map<String, Object> templateModel) throws IOException, TemplateException, MessagingException {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("mail.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        sendHtmlMessage(email, htmlBody);
    }

    private void sendHtmlMessage(String to, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("Airport");
        helper.setTo(to);
        helper.setSubject("Reset password instructions");
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

}
