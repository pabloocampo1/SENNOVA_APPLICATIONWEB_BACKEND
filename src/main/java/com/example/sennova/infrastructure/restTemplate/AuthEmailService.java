package com.example.sennova.infrastructure.restTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthEmailService {

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

    public void sendAccessLink(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("Accede a tu cuenta");

            String frontendUrl = "http://localhost:5173/signIn/change-password/";
            String accessLink = frontendUrl  + token;

            String htmlContent = """
            <div style="font-family: Arial, sans-serif; color: #333; text-align: center;">
                <h2 style="color: #4CAF50;">🔐 Acceso seguro</h2>
                <p>Haz clic en el siguiente botón para acceder:</p>

                <a href="%s"
                   style="
                        display: inline-block;
                        margin: 20px 0;
                        padding: 14px 28px;
                        background-color: #4CAF50;
                        color: #fff;
                        text-decoration: none;
                        font-size: 16px;
                        border-radius: 6px;
                   ">
                    Acceder a mi cuenta
                </a>

                <p>Este enlace es válido por <strong>10 minutos</strong> y solo puede usarse una vez.</p>

                <p style="font-size: 12px; color: #888;">
                    Si no solicitaste este acceso, ignora este correo.
                </p>
            </div>
        """.formatted(accessLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el enlace de acceso", e);
        }
    }


}
