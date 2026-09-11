package com.hua.myElga.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final ResourceLoader resourceLoader;

    private final JavaMailSender mailSender;

    public EmailService(ResourceLoader resourceLoader, JavaMailSender mailSender) {
        this.resourceLoader = resourceLoader;
        this.mailSender = mailSender;
    }

    public void sendLoginNotification(String email, String username, String date, String location, String ip, String browser, String platform) throws IOException, MessagingException {
        // Load html template
        Resource template = resourceLoader.getResource("classpath:templates/login-notification.html");

        String html;

        try (InputStream inputStream = template.getInputStream()) {

            byte[] templateBytes = inputStream.readAllBytes();

            html = new String(templateBytes, StandardCharsets.UTF_8);
        }

        // Prepare the file
        html = html
                .replace("{{ USERNAME }}", username)
                .replace("{{ DATE }}", date)
                .replace("{{ LOCATION }}", location)
                .replace("{{ IP }}", ip)
                .replace("{{ BROWSER }}", browser)
                .replace("{{ PLATFORM }}", platform);

        // Load embedded logo
        Resource logo = resourceLoader.getResource("classpath:static/myelga_logo.svg");

        // Create email
        MimeMessage emailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Ήσασταν εσείς; Νέα σύνδεση στο λογαριασμό σας.");
        helper.setText(html, true);
        helper.addInline("myelgaLogo", logo, "image/svg+xml");

        // Send email
        mailSender.send(emailMessage);
    }
}
