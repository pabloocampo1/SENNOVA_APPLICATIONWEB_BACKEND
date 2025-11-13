package com.example.sennova.infrastructure.restTemplate;

import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.ReagentModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReagentEmail {
    private final JavaMailSender javaMailSender;

    @Autowired
    public ReagentEmail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmailExpiration(String to, List<ReagentModel> reagents) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("🧪 Reactivos que se vencen hoy — " + LocalDate.now());


            StringBuilder reagentTableHtml = new StringBuilder();
            reagentTableHtml.append("""
                        <table style="width:100%; border-collapse: collapse; margin-top: 15px;">
                            <thead style="background-color: #2196F3; color: white;">
                                <tr>
                                    <th style="padding: 10px; text-align: left;">Nombre del reactivo</th>
                                    <th style="padding: 10px; text-align: left;">Estado</th>
                                    <th style="padding: 10px; text-align: left;">Fecha de vencimiento</th>
                                </tr>
                            </thead>
                            <tbody>
                    """);

            for (ReagentModel reagent : reagents) {
                reagentTableHtml.append(String.format("""
                                    <tr style="border-bottom: 1px solid #ddd;">
                                        <td style="padding: 10px;">%s</td>
                                        <td style="padding: 10px;">%s</td>
                                        <td style="padding: 10px;">%s</td>
                                    </tr>
                                """,
                        reagent.getReagentName(),
                        reagent.getState(),
                        reagent.getExpirationDate()
                ));
            }

            reagentTableHtml.append("""
                            </tbody>
                        </table>
                    """);


            String htmlContent = String.format("""
                        <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f7fb; padding: 30px;">
                            <div style="max-width: 600px; margin: auto; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 3px 8px rgba(0,0,0,0.1);">
                                <div style="background-color: #2196F3; color: white; padding: 15px 25px;">
                                    <h2 style="margin: 0;">🔔 Aviso de reactivos próximos a vencer</h2>
                                </div>
                                <div style="padding: 25px;">
                                    <p style="font-size: 15px; color: #333;">Hola 👋,</p>
                                    <p style="font-size: 15px; color: #333;">
                                        Estos son los reactivos que <b>se vencen hoy (%s)</b> o requieren revisión:
                                    </p>
                                    %s
                                    <p style="margin-top: 25px; font-size: 13px; color: #666;">
                                        🧠 Revisa el inventario para realizar las acciones necesarias.
                                    </p>
                                    <hr style="border: none; border-top: 1px solid #ddd; margin: 25px 0;">
                                    <p style="text-align: center; font-size: 12px; color: #999;">
                                        📩 Este correo fue generado automáticamente por el sistema de Sennova.<br>
                                        Por favor, no respondas a este mensaje.
                                    </p>
                                </div>
                            </div>
                        </div>
                    """, LocalDate.now(), reagentTableHtml.toString());

            helper.setText(htmlContent, true);
            javaMailSender.send(message);

            System.out.println("📧 Correo enviado correctamente a " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("❌ Error enviando el correo de expiración de reactivos", e);
        }
    }


}
