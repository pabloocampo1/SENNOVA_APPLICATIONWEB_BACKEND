package com.example.sennova.infrastructure.restTemplate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class TestRequestEmailService {

    private final JavaMailSender mailSender;

    public void sendEmailNewQuotation(String to, String testRequestCode, String customerName, String quotationDetailsLink){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("📄 Nueva cotización disponible");

            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; color: #333; text-align: center;">
                        <h2 style="color: #4CAF50;">¡Hola %s!</h2>
                        <p>Se ha generado una <strong>nueva cotización de ensayo</strong> para ti.</p>
                        
                        <p><strong>Código de la cotización:</strong> <span style="color:#4CAF50;">%s</span></p>
                        
                        <p>Puedes revisar todos los detalles de la cotización haciendo clic en el botón a continuación:</p>
                        
                        <a href="%s" style="
                            display: inline-block;
                            padding: 12px 24px;
                            margin: 20px 0;
                            font-size: 16px;
                            color: #fff;
                            background-color: #4CAF50;
                            text-decoration: none;
                            border-radius: 6px;
                        ">Ver Cotización</a>
                         
                        <p style="font-size: 12px; color: #888;">
                            Este es un correo automático. No respondas a este mensaje.
                        </p>
                    </div>
                    """.formatted(customerName, testRequestCode, quotationDetailsLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el correo de nueva cotización", e);
        }
    }
}
