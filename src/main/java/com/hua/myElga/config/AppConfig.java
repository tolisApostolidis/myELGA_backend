package com.hua.myElga.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@Configuration
public class  AppConfig {
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    //// Creates and configures a JavaMailSender bean used for sending emails through Gmail's SMTP server. ////
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    //// Creates a Password Encoder bean used to encode passwords ////
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //// Creates and configures the OpenAPI documentation for the application. ////
    //// Provides basic metadata for the Swagger UI and allows automatic generation of the REST API documentation for the myElga project. ////
    @Bean
    public OpenAPI openAPI() {
        OpenAPI info = new OpenAPI()
                .info(new Info().title("myElga API")
                        .description("This API used for myElga academic project")
                        .contact(new Contact().name("2021010")
                                .email("it2021010@hua.gr"))
                );

        return info;
    }
}