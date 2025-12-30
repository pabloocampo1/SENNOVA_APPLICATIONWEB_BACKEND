package com.example.sennova.infrastructure.restTemplate;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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

    public void sendEmailTestRequestDueDate(List<TestRequestModel> testRequestModels, String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("📄 Entrega de ensayo programada para hoy " + LocalDate.now());

            // Construir HTML dinámico
            StringBuilder htmlContent = new StringBuilder();

            // Encabezado con text block y placeholder
            htmlContent.append("<div style=\"font-family: Arial, sans-serif; color: #333;\">"
                    + "<h2 style=\"color: #4CAF50;\">¡Hola " + name + "!</h2>"
                    + "<p>Se ha generado una lista de <strong>ensayos programados para entregar hoy</strong>:</p>"
                    + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">"
                    + "<thead>"
                    + "<tr style=\"background-color: #e3f2fd;\">"
                    + "<th style=\"border: 1px solid #ddd; text-align: left;\">Código del Ensayo</th>"
                    + "<th style=\"border: 1px solid #ddd; text-align: left;\">Muestras</th>"
                    + "</tr>"
                    + "</thead>"
                    + "<tbody>");


            for (TestRequestModel testRequest : testRequestModels) {
                htmlContent.append("<tr>");
                htmlContent.append("<td style='border: 1px solid #ddd; vertical-align: top;'>")
                        .append(testRequest.getRequestCode())
                        .append("</td>");

                htmlContent.append("<td style='border: 1px solid #ddd;'>");
                htmlContent.append("<table width='100%' cellpadding='3' cellspacing='0' style='border-collapse: collapse;'>");
                htmlContent.append("<thead><tr style='background-color: #f1f1f1;'>")
                        .append("<th style='border: 1px solid #ddd;'>Código de la muestra</th>")
                        .append("<th style='border: 1px solid #ddd;'>Nombre de la matriz</th>")
                        .append("</tr></thead>");
                htmlContent.append("<tbody>");

                for (SampleModel sample : testRequest.getSamples()) {
                    htmlContent.append("<tr>");
                    htmlContent.append("<td style='border: 1px solid #ddd;'>")
                            .append(sample.getSampleCode())
                            .append("</td>");
                    htmlContent.append("<td style='border: 1px solid #ddd;'>")
                            .append(sample.getMatrix())
                            .append("</td>");
                    htmlContent.append("</tr>");
                }

                htmlContent.append("</tbody></table>");
                htmlContent.append("</td>");
                htmlContent.append("</tr>");
            }

            // Cierre del HTML
            htmlContent.append("""
                    </tbody>
                </table>
                <p style="margin-top: 20px;">Por favor, revise los ensayos y asegúrese de cumplir con las fechas de entrega.</p>
                <p style="font-size: 12px; color: #888;">Este es un correo automático. No responda este mensaje.</p>
            </div>
            """);

            helper.setText(htmlContent.toString(), true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotificationTestRequestCompleted(String testRequestCode, String email, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(email);
            helper.setSubject("✅ Ensayo completado: " + testRequestCode);

            // Contenido HTML
            String htmlContent = """
                <div style="font-family: Arial, sans-serif; color: #333;">
                    <h2 style="color: #4CAF50;">¡Hola %s!</h2>
                    <p>El ensayo con código <strong>%s</strong> ha sido completado exitosamente.</p>
                    <p>Por favor, revise los resultados y genere el informe correspondiente para enviarlo al cliente.</p>
                    <p style="margin-top: 20px;">Gracias por su atención.</p>
                    <p style="font-size: 12px; color: #888;">Este es un correo automático. No responda este mensaje.</p>
                </div>
                """.formatted(userName, testRequestCode);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando notificación de ensayo completado", e);
        }
    }

   /*
    public void sendEmailFinishTestRequest( String to, MultipartFile attachment) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("no-reply@sennova.com");
        helper.setTo(to);
        helper.setSubject("🛠️ Equipos que requieren mantenimiento — " + LocalDate.now());

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(
                    attachment.getOriginalFilename(),
                    attachment
            );
        }

        helper.setText(htmlContent, true);
        mailSender.send(message);

    }

    */




}
