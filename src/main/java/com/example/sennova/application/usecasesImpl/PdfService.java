package com.example.sennova.application.usecasesImpl;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;






// en este servicio se debe de cambiar los dto, pasar test request y sla lista de samples, adicionalemnte el cliente


@Service
public class PdfService {


    public static class DatosInformeDTO {

        public String solicitante;
        public String empresa;
        public String direccion;
        public String cedulaNit;
        public String correo;
        public String noSolicitud;
        public String telefono;


        public String fechaIngreso;
        public String fechaEmision;
        public String nombreDirector;
        public List<PdfService.ResultadoAnalisis> resultados;




        public DatosInformeDTO(String solicitante, String empresa, String direccion, String cedulaNit, String correo, String noSolicitud, String telefono, String fechaIngreso, String fechaEmision, String nombreDirector, List<ResultadoAnalisis> resultados) {
            this.solicitante = solicitante;
            this.empresa = empresa;
            this.direccion = direccion;
            this.cedulaNit = cedulaNit;
            this.correo = correo;
            this.noSolicitud = noSolicitud;
            this.telefono = telefono;
            this.fechaIngreso = fechaIngreso;
            this.fechaEmision = fechaEmision;
            this.nombreDirector = nombreDirector;
            this.resultados = resultados;
        }

        public String getSolicitante() {
            return solicitante;
        }

        public void setSolicitante(String solicitante) {
            this.solicitante = solicitante;
        }

        public String getEmpresa() {
            return empresa;
        }

        public void setEmpresa(String empresa) {
            this.empresa = empresa;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getCedulaNit() {
            return cedulaNit;
        }

        public void setCedulaNit(String cedulaNit) {
            this.cedulaNit = cedulaNit;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getNoSolicitud() {
            return noSolicitud;
        }

        public void setNoSolicitud(String noSolicitud) {
            this.noSolicitud = noSolicitud;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getFechaIngreso() {
            return fechaIngreso;
        }

        public void setFechaIngreso(String fechaIngreso) {
            this.fechaIngreso = fechaIngreso;
        }

        public String getFechaEmision() {
            return fechaEmision;
        }

        public void setFechaEmision(String fechaEmision) {
            this.fechaEmision = fechaEmision;
        }

        public String getNombreDirector() {
            return nombreDirector;
        }

        public void setNombreDirector(String nombreDirector) {
            this.nombreDirector = nombreDirector;
        }

        public List<ResultadoAnalisis> getResultados() {
            return resultados;
        }

        public void setResultados(List<ResultadoAnalisis> resultados) {
            this.resultados = resultados;
        }
    }


    public static class ResultadoAnalisis {
        public int item;
        public String analisis;
        public String metodo;
        public String resultado;
        public String incertidumbre;
        public String valorReferencia;
        public String fechaAnalisis;

        public int getItem() {
            return item;
        }

        public void setItem(int item) {
            this.item = item;
        }

        public String getAnalisis() {
            return analisis;
        }

        public void setAnalisis(String analisis) {
            this.analisis = analisis;
        }

        public String getMetodo() {
            return metodo;
        }

        public void setMetodo(String metodo) {
            this.metodo = metodo;
        }

        public String getResultado() {
            return resultado;
        }

        public void setResultado(String resultado) {
            this.resultado = resultado;
        }

        public String getIncertidumbre() {
            return incertidumbre;
        }

        public void setIncertidumbre(String incertidumbre) {
            this.incertidumbre = incertidumbre;
        }

        public String getValorReferencia() {
            return valorReferencia;
        }

        public void setValorReferencia(String valorReferencia) {
            this.valorReferencia = valorReferencia;
        }

        public String getFechaAnalisis() {
            return fechaAnalisis;
        }

        public void setFechaAnalisis(String fechaAnalisis) {
            this.fechaAnalisis = fechaAnalisis;
        }

        public ResultadoAnalisis(int item, String analisis, String metodo, String resultado,
                                 String incertidumbre, String valorReferencia, String fechaAnalisis) {
            this.item = item;
            this.analisis = analisis;
            this.metodo = metodo;
            this.resultado = resultado;
            this.incertidumbre = incertidumbre;
            this.valorReferencia = valorReferencia;
            this.fechaAnalisis = fechaAnalisis;
        }
    }
    

    public byte[] generarInformePdf(
            DatosInformeDTO datos, MultipartFile firmaFile
    ) throws Exception {

        String imgHtml = "";
        if (firmaFile != null && !firmaFile.isEmpty()) {
            byte[] bytes = firmaFile.getBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String extension = firmaFile.getContentType();
            String firmaBase64 = "data:" + extension + ";base64," + base64;


            imgHtml = "<img src='" + firmaBase64 + "' class='signature-image' alt='Firma' />";
        } else {

            imgHtml = "<div style='height: 40px;'></div>";
        }

        StringBuilder sbResultados = new StringBuilder();
        for (ResultadoAnalisis r : datos.getResultados()) {
            sbResultados.append("<tr>")
                    .append("<td class='center'>").append(r.item).append("</td>")
                    .append("<td>").append(escapeHtml(r.analisis)).append("</td>")
                    .append("<td class='small-text'>").append(escapeHtml(r.metodo)).append("</td>")
                    .append("<td class='center bold'>").append(escapeHtml(r.resultado)).append("</td>")
                    .append("<td class='center'>").append(escapeHtml(r.incertidumbre)).append("</td>")
                    .append("<td class='center'>").append(escapeHtml(r.valorReferencia)).append("</td>")
                    .append("<td class='center small-text'>").append(escapeHtml(r.fechaAnalisis)).append("</td>")
                    .append("</tr>");
        }

        String html = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8" />
            <style>
                /* Page setup: Define margins and running elements for header/footer */
                @page {
                    margin: 4.5cm 1.5cm 2.5cm 1.5cm;
                    @top-center { content: element(header); }
                    @bottom-center { content: element(footer); }
                }

                body { 
                    font-family: 'Helvetica', 'Arial', sans-serif; 
                    font-size: 9pt; 
                    color: #333; 
                }

                /* Recurring elements mapping */
                #header { position: running(header); width: 100%%; }
                #footer { position: running(footer); width: 100%%; }

                /* Header Styles */
                .header-table { 
                    border: none; 
                    width: 100%%; 
                    border-bottom: 1px solid #000; 
                    padding-bottom: 10px; 
                }
                .header-table td { border: none; padding: 0; }
                .header-left { width: 15%%; }
                .header-center { 
                    width: 65%%; 
                    text-align: center; 
                    font-weight: bold; 
                    font-size: 11pt; 
                }
                .header-right { 
                    width: 20%%; 
                    font-size: 8pt; 
                    text-align: right; 
                }

                /* Footer Styles */
                .footer-content { 
                    text-align: center; 
                    font-size: 7pt; 
                    color: #888; 
                    border-top: 0.5pt solid #ccc; 
                    padding-top: 5px; 
                }
                .pagenum:before { content: counter(page); }
                .pagecount:before { content: counter(pages); }
                
                .software-brand {
                    font-size: 6.5pt;
                    color: #999;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                    margin-bottom: 10px;
                }
                .brand-name { color: #354a5f; font-weight: bold; }

                /* General Table Styles */
                table { 
                    border-collapse: collapse; 
                    width: 100%%; 
                    margin-bottom: 12px; 
                    table-layout: fixed; 
                }
                th, td { 
                    border: 0.5pt solid #444; 
                    padding: 5px 8px; 
                    vertical-align: middle; 
                    word-wrap: break-word; 
                }

                .report-number { 
                    text-align: center; 
                    font-weight: bold; 
                    font-size: 12pt; 
                    margin: 30px 0; 
                    text-decoration: underline;  
                }
                
                .section-title { 
                    background-color: #e2e2e2; 
                    font-weight: bold; 
                    font-size: 8.5pt; 
                    text-transform: uppercase; 
                }

                /* Cell specific styles */
                .label { font-weight: bold; background-color: #f2f2f2; width: 25%%; }
                .value { width: 25%%; }
                .value-full { width: 75%%; }

                /* Analytical Results Table */
                .results-table th { 
                    background-color: #354a5f; 
                    color: white; 
                    text-align: center; 
                    margin-top: 20px;
                }
                .center { text-align: center; }

                /* Declaration Box */
                .declaration-box { 
                    margin-top: 20px; 
                    border: 0.5pt solid #ccc; 
                    padding: 8px; 
                    background-color: #fafafa; 
                }
                .declaration-title { font-weight: bold; font-size: 8pt; border-bottom: 1px solid #ddd; }
                .declaration-text { font-size: 7pt; color: #444; text-align: justify; }
                .declaration-list { margin: 5px 0 0 15px; padding: 0; }
                
                /* Conventions Table */
                .convenciones-table { margin-top: 10px; border: 0.5pt solid #aaa; }
                .convenciones-table td { border: none; font-size: 7pt; padding: 2px 5px; color: #444; }
                .conv-label { font-weight: bold; color: #000; }

                .footer-note { 
                    margin-top: 10px; 
                    font-size: 9pt; 
                    font-style: italic; 
                    text-align: center; 
                    color: #888; 
                }

                /* Signature Section */
                .signature-container { margin-top: 30px; width: 300px; }
                .signature-label { font-weight: bold; margin-bottom: 5px; display: block; }
                .signature-line { border-top: 1px solid #000; margin-top: 5px; padding-top: 5px; }
                .signature-image { max-height: 60px; margin-bottom: -10px; margin-left: 20px; }
                .director-name { font-weight: bold; font-size: 9pt; display: block; }
                .director-charge { font-size: 7.5pt; color: #555; display: block; }
            </style>
        </head>
        <body>
            <div id="header">
                <table class="header-table">
                    <tr>
                        <td class="header-left">
                            <img src="https://senacertificados.co/wp-content/uploads/2021/10/logo-de-SENA-png-Negro-300x300-1.png" width="60" />
                        </td>
                        <td class="header-center">
                            LABORATORIO DE SERVICIOS TECNOLÓGICOS<br/>
                            SENA C.R.N.R LA SALADA<br/>
                            Informe de resultados de análisis
                        </td>
                        <td class="header-right">
                            CÓDIGO: LST-F-001<br/>
                            VERSIÓN: 04<br/>
                            Fecha: 2024-03-22
                        </td>
                    </tr>
                </table>
            </div>

            <div id="footer">
                <div class="footer-content">
                    Servicio Nacional de Aprendizaje – SENA<br/>
                    Centro de los Recursos Naturales Renovables La Salada<br/>
                    km 6 vía la Pintada, Caldas – Antioquia<br/>
                    E-mail: lasaladalst@sena.edu.co<br/><br/>
                    <div class="software-brand">
                        Report generated by <span class="brand-name">LABSYS ONE SOFTWARE 2025</span> &copy;
                    </div>
                    Page <span class="pagenum"></span> of <span class="pagecount"></span>
                </div>
            </div>

            <div class="report-number">INFORME DE ENSAYO N ° M25-132</div>

            <table>
                <tr><th colspan="4" class="section-title">INFORMACIÓN GENERAL</th></tr>
                <tr><td class="label">Solicitante:</td><td colspan="3">%s</td></tr>
                <tr><td class="label">Empresa:</td><td colspan="3">%s</td></tr>
                <tr><td class="label">Dirección:</td><td colspan="3">%s</td></tr>
                <tr>
                    <td class="label">Cedula/NIT:</td><td>%s</td>
                    <td class="label" style="width:18%%;">Correo electrónico:</td><td>%s</td>
                </tr>
                <tr>
                    <td class="label">No. Solicitud:</td><td>%s</td>
                    <td class="label" style="width:18%%;">Teléfono:</td><td>%s</td>
                </tr>
            </table>

            <table>
                <tr><th colspan="4" class="section-title">2. INFORMACIÓN DE LA MUESTRA</th></tr>
                <tr>
                    <td class="label">Fecha ingreso:</td><td class="value">%s</td>
                    <td class="label">Matriz:</td><td class="value">Agua tratada</td>
                </tr>
                <tr>
                    <td class="label">Fecha emisión:</td><td class="value">%s</td>
                    <td class="label">Lugar de Recolección:</td><td class="value">Embotellada Pool</td>
                </tr>
                <tr>
                    <td class="label">Identificación Interna:</td><td class="value" colspan="3"><b>M25-133</b></td>
                </tr>
                <tr>
                    <td class="label">Especificaciones del cliente:</td>
                    <td class="value-full" colspan="3">Temperatura de recepción: 23,3°C. Muestra entregada por el cliente en condiciones óptimas.</td>
                </tr>
            </table>

            <div class="declaration-box">
                <span class="declaration-title">DECLARACIÓN</span>
                <div class="declaration-text">
                    <ul class="declaration-list">
                        <li>Los resultados de este informe sólo afectan al material sometido al ensayo, no se realiza muestreo.</li>
                        <li>El Cliente asume en su totalidad la responsabilidad del muestreo.</li>
                        <li>Las muestras serán ingresadas al sistema de lunes a viernes en horario de recepción. Días no hábiles se ingresan al siguiente día hábil.</li>
                        <li>Este informe no debe reproducirse de forma parcial sin autorización.</li>
                    </ul>
                </div>
            </div>

            <div style="font-weight: bold; font-size: 8pt; margin-top: 10px;">CONVENCIONES:</div>
            <table class="convenciones-table">
                <tr>
                    <td><span class="conv-label">UFC:</span> Unidades Formadoras de Colonias</td>
                    <td><span class="conv-label">NA:</span> No aplica</td>
                    <td><span class="conv-label">ppb:</span> partes por billón (µg/L)</td>
                </tr>
            </table>

            <table class="results-table">
                <thead>
                    <tr><th colspan="7" class="section-title" style="color:white;">3. RESULTADOS DE ANÁLISIS</th></tr>
                    <tr>
                        <th style="width: 5%%;">Ítem</th>
                        <th style="width: 20%%;">Análisis</th>
                        <th style="width: 22%%;">Método/Técnica</th>
                        <th style="width: 13%%;">Resultado</th>
                        <th style="width: 10%%;">Incert.</th>
                        <th style="width: 18%%;">Valor Ref.*</th>
                        <th style="width: 12%%;">Fecha Anal.</th>
                    </tr>
                </thead>
                <tbody>
                    %s
                </tbody>
            </table>

            <div class="signature-container">
                <span class="signature-label">Aprueba:</span>
                <div class="signature-img-wrapper">
                    %s
                </div>
                <div class="signature-line">
                    <span class="director-name">%s</span>
                    <span class="director-charge">Director Técnico Laboratorio de Servicios Tecnológicos</span>
                    <span class="director-charge">C.R.N.R La Salada</span>
                </div>
            </div>
            <div class="footer-note">--- Fin del Informe ---</div>
        </body>
        </html>
       """.formatted(

                escapeHtml(datos.getSolicitante()),
                escapeHtml(datos.getEmpresa()),
                escapeHtml(datos.getDireccion()),
                escapeHtml(datos.getCedulaNit()),
                escapeHtml(datos.getCorreo()),
                escapeHtml(datos.getNoSolicitud()),
                escapeHtml(datos.getTelefono()),


                escapeHtml(datos.getFechaIngreso()),
                escapeHtml(datos.getFechaEmision()),


                sbResultados.toString(),
                imgHtml,
                escapeHtml(datos.getNombreDirector())
        );

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}



