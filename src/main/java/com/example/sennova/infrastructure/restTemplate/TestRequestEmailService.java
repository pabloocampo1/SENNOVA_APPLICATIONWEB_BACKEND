package com.example.sennova.infrastructure.restTemplate;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
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


    /**
     *
     *
     * @param customer
     * @param pdfDocument
     * @param responsibleName
     * @param sampleCode
     * @throws MessagingException 
     */
    public void sendSampleReport(
            CustomerModel customer,
            byte[] pdfDocument,
            String responsibleName,
            String sampleCode
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


        helper.setTo(customer.getEmail());
        helper.setFrom("no-reply@labsys.com");
        helper.setSubject("Informe de resultado - Muestra " + sampleCode);


        String htmlBody = "<p>Estimado/a " + customer.getCustomerName() + ",</p>"
                + "<p>Adjunto encontrará el informe del resultado de su muestra.</p>"
                + "<p>Atentamente,<br/>" + responsibleName + "</p>";

        helper.setText(htmlBody, true);


        helper.addAttachment("Informe_" + sampleCode + ".pdf", new org.springframework.core.io.ByteArrayResource(pdfDocument));

        
        mailSender.send(message);
    }


    public void sendFinalReport(
            CustomerModel customer,
            String testRequestCode,
            List<byte[]> generatedReports,
            List<MultipartFile> additionalDocuments,
            String notes,
            String responsibleName,
            String responsibleRole
    ) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(customer.getEmail());
            helper.setSubject("📄 Informe final de ensayo - " + testRequestCode);

            // -------- HTML BODY CON MARCA --------
            String htmlContent = """
        <div style="font-family: Arial, sans-serif; color: #333;">
            <h2 style="color: #4CAF50;">Informe final de ensayo</h2>

            <p>Estimado/a <strong>%s</strong>,</p>

            <p>Adjunto encontrará el <strong>informe final del ensayo</strong>
            correspondiente a la solicitud con código:</p>

            <p style="font-size: 16px;">
                <strong style="color:#4CAF50;">%s</strong>
            </p>

            %s

            <p style="margin-top: 20px;">
                Este informe fue generado por:
                <br/>
                <strong>%s</strong><br/>
                %s
            </p>

            <div style="font-size:6.5pt; color:#999; text-transform:uppercase; letter-spacing:1px; margin-top:15px;">
                Report generated by <span style="font-weight:bold;">LABSYS ONE SOFTWARE 2025</span> &copy;
            </div>

            <p style="font-size: 12px; color: #888;">
                Este es un correo automático. No responda este mensaje.
            </p>
        </div>
        """.formatted(
                    customer.getCustomerName(),
                    testRequestCode,
                    (notes != null && !notes.isBlank())
                            ? "<p><strong>Notas adicionales:</strong><br/>" + notes + "</p>"
                            : "",
                    responsibleName,
                    responsibleRole != null ? responsibleRole : ""
            );

            helper.setText(htmlContent, true);

            // -------- ATTACH GENERATED REPORTS --------
            int reportIndex = 1;
            for (byte[] report : generatedReports) {
                helper.addAttachment(
                        "Informe"+".pdf",
                        new org.springframework.core.io.ByteArrayResource(report)
                );
            }

            // -------- ATTACH ADDITIONAL DOCUMENTS --------
            if (additionalDocuments != null) {
                for (MultipartFile document : additionalDocuments) {
                    helper.addAttachment(
                            document.getOriginalFilename(),
                            document
                    );
                }
            }

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando el informe final del ensayo", e);
        }
    }



    /**
     * Envía un correo personalizado con un mensaje adicional, un archivo adjunto
     * y una lista de puntos clave.
     *
     * @param to
     * @param customerName Nombre del cliente
     * @param customMessage Mensaje adicional redactado por el usuario
     * @param itemsList Lista de strings (ej. observaciones o requisitos)
     * @param attachment Archivo PDF u otro formato
     */
    public void sendEmailWithAttachmentAndList(
            String to,
            String customerName,
            String customMessage,
            List<String> itemsList,
            MultipartFile attachment
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("✅ ACEPTACIÓN COTIZACIÓN - SOLICITUD");


            StringBuilder listHtml = new StringBuilder("<ul style='color: #555;'>");
            if (itemsList != null && !itemsList.isEmpty()) {
                for (String item : itemsList) {
                    listHtml.append("<li>").append(item).append("</li>");
                }
            }
            listHtml.append("</ul>");


            // 1. Preparamos el aviso del adjunto solo si el archivo existe
            String attachmentNote = (attachment != null && !attachment.isEmpty())
                    ? """
      <div style="margin: 20px 0; padding: 12px; border: 1px dashed #4CAF50; border-radius: 6px; background-color: #f0fdf4;">
          <p style="margin: 0; font-size: 14px; color: #1e4010;">
              📎 <strong>Nota:</strong> Se ha adjuntado un documento de apoyo a este correo para su revisión.
          </p>
      </div>
      """
                    : ""; // Si no hay archivo, no ponemos nada

// 2. Metemos esa variable en el HTML principal
            String htmlContent = """
    <div style="font-family: 'Segoe UI', Arial, sans-serif; line-height: 1.6; color: #444; max-width: 600px; border: 1px solid #eee; padding: 20px; border-radius: 8px;">
        <div style="border-bottom: 2px solid #4CAF50; padding-bottom: 10px; margin-bottom: 20px;">
            <span style="font-size: 11px; font-weight: bold; color: #4CAF50; text-transform: uppercase; letter-spacing: 1px;">Notificación Oficial</span>
            <h2 style="color: #2c3e50; margin: 5px 0;">¡Hola, %s! 👋</h2>
        </div>

        <p>Le informamos que se ha aceptado la solicitud de ensayo. Siga las indicaciones de este correo para la entrega de muestras.</p>
        
        <div style="background-color: #f9fafb; border-left: 4px solid #4CAF50; padding: 15px; margin: 20px 0; border-radius: 0 4px 4px 0;">
            <p style="margin-top: 0; font-weight: bold; color: #1e4010;">💬 Notas adicionales del laboratorio:</p>
            <i style="color: #374151;">"%s"</i>
        </div>

        <div style="margin: 25px 0;">
            <p style="margin-bottom: 10px;"><strong>🧪 Muestras seleccionadas:</strong></p>
            <div style="background: #ffffff; border: 1px solid #e5e7eb; border-radius: 6px; padding: 12px;">
                %s
            </div>
        </div>

        %s 
        
        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0 15px 0;">
        <div style="text-align: center;">
            <p style="font-size: 11px; color: #777; margin-bottom: 5px;">
                Este mensaje fue emitido automáticamente por el <strong>Sistema de Gestión de Laboratorio SENNOVA</strong>.
                <br>Centro de los Recursos Naturales Renovables La Salada - SENA.
            </p>
            <p style="font-size: 10px; color: #aaa; letter-spacing: 1px; text-transform: uppercase; font-weight: bold;">
                Powered by LABSYS ONE SOFTWARE 2026 ©
            </p>
        </div>
    </div>
    """.formatted(
                    customerName,
                    (customMessage != null && !customMessage.isBlank()) ? customMessage : "No se registraron notas adicionales.",
                    listHtml.toString(),
                    attachmentNote
            );

            helper.setText(htmlContent, true);

            // Agregar el archivo adjunto
            if (attachment != null && !attachment.isEmpty()) {
                helper.addAttachment(
                        attachment.getOriginalFilename() != null ? attachment.getOriginalFilename() : "documento.pdf",
                        attachment
                );
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el correo con adjunto: " + e.getMessage(), e);
        }
    }








}
