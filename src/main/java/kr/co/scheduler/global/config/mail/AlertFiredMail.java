package kr.co.scheduler.global.config.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class AlertFiredMail {
    private final JavaMailSender javaMailSender;

    /**
     * createMessage: 메세지 생성
     */
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("SPAP 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1 style='color: #956be8';> 안녕하세요.</h1>";
        msgg += "<h1 style='color: #956be8';> 당신의 일정 관리 매니저 SPAP 입니다.</h1>";
        msgg += "<br><br>";
        msgg += "<p>30일간 미접속 사용자에게 전송되는 메일입니다.<p>";
        msgg += "<p>회원님의 개인정보보호 및 시스템 유지 목적으로 회원님의 계정이 탈퇴된 점 양해부탁드립니다.<p>";
        msgg += "<br><br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("spoof332@naver.com", "김진형"));// 보내는 사람

        return message;
    }


    /**
     * sendMessage: createMessage 를 통해 메세지 전송
     */
    public void sendMessage(String to) throws Exception {

        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to); // 메일 발송

        try {// 예외처리

            javaMailSender.send(message);
        } catch (MailException e) {

            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}