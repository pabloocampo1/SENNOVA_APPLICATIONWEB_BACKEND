package com.example.sennova.infrastructure.restTemplate;

import com.example.sennova.domain.model.EquipmentModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EquipmentEmail {
    private final JavaMailSender mailSender;

    public EquipmentEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailMaintenance(String to, List<EquipmentModel> equipos) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("🛠️ Equipos que requieren mantenimiento — " + LocalDate.now());

            // Construir tabla de equipos
            StringBuilder equipmentTableHtml = new StringBuilder();
            equipmentTableHtml.append("""
                        <table style="width:100%; border-collapse: collapse; margin-top: 15px;">
                            <thead style="background-color: #2196F3; color: white;">
                                <tr>
                                    <th style="padding: 10px; text-align: left;">Código interno</th>
                                    <th style="padding: 10px; text-align: left;">Nombre del equipo</th>
                                    <th style="padding: 10px; text-align: left;">Fecha de mantenimiento</th>
                                    <th style="padding: 10px; text-align: left;">Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                    """);

            for (EquipmentModel equipo : equipos) {
                equipmentTableHtml.append(String.format("""
                                    <tr style="border-bottom: 1px solid #ddd;">
                                        <td style="padding: 10px;">%s</td>
                                        <td style="padding: 10px;">%s</td>
                                        <td style="padding: 10px;">%s</td>
                                        <td style="padding: 10px;">%s</td>
                                    </tr>
                                """,
                        equipo.getInternalCode(),
                        equipo.getEquipmentName(),
                        equipo.getMaintenanceDate(),
                        equipo.getState()
                ));
            }

            equipmentTableHtml.append("""
                            </tbody>
                        </table>
                    """);

            // HTML general del correo
            String htmlContent = String.format("""
                        <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f7fb; padding: 30px;">
                            <div style="max-width: 600px; margin: auto; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 3px 8px rgba(0,0,0,0.1);">
                                <div style="background-color: #2196F3; color: white; padding: 15px 25px;">
                                    <h2 style="margin: 0;">🔧 Aviso de mantenimiento programado</h2>
                                </div>
                                <div style="padding: 25px;">
                                    <p style="font-size: 15px; color: #333;">Hola 👋,</p>
                                    <p style="font-size: 15px; color: #333;">
                                        Estos son los equipos que <b>requieren mantenimiento</b> o revisión al día de hoy (%s):
                                    </p>
                                    %s
                                    <p style="margin-top: 25px; font-size: 13px; color: #666;">
                                        ⚙️ Por favor, verifica su estado y programa las acciones necesarias.
                                    </p>
                                    <hr style="border: none; border-top: 1px solid #ddd; margin: 25px 0;">
                                    <p style="text-align: center; font-size: 12px; color: #999;">
                                        📩 Este correo fue generado automáticamente por el sistema de Sennova.<br>
                                        Por favor, no respondas a este mensaje.
                                    </p>
                                </div>
                            </div>
                        </div>
                    """, LocalDate.now(), equipmentTableHtml.toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);

            System.out.println("📧 Correo de mantenimiento enviado correctamente a " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("❌ Error enviando el correo de mantenimiento de equipos", e);
        }
    }
}
