package kr.co.scheduler.global.config.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.username}")
    private String MailUsername;

    @Value("${mail.password}")
    private String MailPassword;

    @Bean
    public JavaMailSender javaMailService() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername(MailUsername);
        javaMailSender.setPassword(MailPassword);

        javaMailSender.setPort(465); // 메일 인증서버 포트

        javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 인증서버 정보 가져오기

        return javaMailSender;
    }

    private Properties getMailProperties() {

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true"); // smtp 인증
        properties.setProperty("mail.smtp.starttls.enable", "true"); // tls 사용
        properties.setProperty("mail.debug", "true"); // debug 사용
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com"); // ssl 인증 서버 등록
        properties.setProperty("mail.smtp.ssl.enable", "true"); // ssl 사용

        return properties;
    }
}
