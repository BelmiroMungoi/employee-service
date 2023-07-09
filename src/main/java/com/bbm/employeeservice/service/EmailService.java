package com.bbm.employeeservice.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.bbm.employeeservice.utils.EmailUtils.getVerificationUrl;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String username;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendHtmlEmail(String name, String destination, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host, token));
            String text = templateEngine.process("emailTemplate", context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Verificacão de Nova Conta");
            helper.setFrom(new InternetAddress(username, "Equipe Técnica - Belmiro Mungoi"));
            helper.setTo(destination);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private MimeMessage getMimeMessage() {
        return javaMailSender.createMimeMessage();
    }
}
