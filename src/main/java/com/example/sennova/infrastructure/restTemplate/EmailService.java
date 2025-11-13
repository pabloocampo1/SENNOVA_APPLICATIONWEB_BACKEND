package com.example.sennova.infrastructure.restTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String to, Integer code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("Tu código de verificación");

            String htmlContent = """
                        <div style="font-family: Arial, sans-serif; color: #333; text-align: center;">
                            <h2 style="color: #4CAF50;">🔑 Verificación de correo</h2>
                            <p>Usa el siguiente código para verificar tu cuenta:</p>
                            <div style="font-size: 28px; font-weight: bold; letter-spacing: 5px; 
                                        margin: 20px 0; color: #4CAF50;">
                                %s
                            </div>
                            <p>Este código expirará en <strong>5 minutos</strong>.</p>
                            <p style="font-size: 12px; color: #888;">
                                Si no solicitaste este código, ignora este mensaje.
                            </p>
                        </div>
                    """.formatted(code);

            helper.setText(htmlContent, true);
            mailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el correo de verificación", e);
        }
    }

}
