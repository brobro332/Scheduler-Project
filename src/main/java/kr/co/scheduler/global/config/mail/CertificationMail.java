package kr.co.scheduler.global.config.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CertificationMail {

    private final JavaMailSender javaMailSender;

    private String ePw;

    /**
     * createMessage: 메세지 생성
     */
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(jakarta.mail.internet.MimeMessage.RecipientType.TO, to);// 보내는 대상
        message.setSubject("SPAP 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1 style='color: #956be8';> 안녕하세요.</h1>";
        msgg += "<h1 style='color: #956be8';> 당신의 일정 관리 매니저 SPAP 입니다.</h1>";
        msgg += "<br>";
        msgg += "<p> 아래 코드를 회원가입 인증란에 입력해주세요.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='font-family:verdana';>";
        msgg += "<hr style='color: #956be8';><br><br>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong>";
        msgg += "<br><br><br><hr style='color: #956be8';></div></div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("spoof332@naver.com", "김진형"));// 보내는 사람

        return message;
    }

    /**
     * createKey: 인증코드 생성
     */
    public String createKey() {

        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int keyIndex = random.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (keyIndex) {
                case 0:
                    key.append((char) ((int) (random.nextInt(26)) + 97));
                    // a~z
                    break;
                case 1:
                    key.append((char) ((int) (random.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    /**
     * sendMessage: createMessage 와 createKey 를 통해 메세지 전송
     */
    public String sendMessage(String to) throws Exception {

        ePw = createKey(); // 랜덤 인증번호 생성

        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to); // 메일 발송

        try {// 예외처리

            javaMailSender.send(message);
        } catch (MailException e) {

            e.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }
}
