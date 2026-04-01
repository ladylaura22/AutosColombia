package com.autoscolombia.service;

import com.autoscolombia.domain.EstadoPago;
import com.autoscolombia.domain.Pago;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ComprobantePdfService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarPdf(Pago pago) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A5, 36, 36, 54, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            Paragraph title = new Paragraph("COMPROBANTE DE PAGO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("AutosColombia", normalFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            addRow(table, "ID Pago:", "#" + pago.getIdPago(), boldFont, normalFont);
            addRow(table, "Fecha/Hora:", pago.getCreadoEn() != null ? pago.getCreadoEn().format(FMT) : "-", boldFont, normalFont);

            String placa = "-";
            if (pago.getRegistroParqueo() != null && pago.getRegistroParqueo().getVehiculo() != null) {
                placa = pago.getRegistroParqueo().getVehiculo().getPlaca();
            } else if (pago.getMensualidad() != null && pago.getMensualidad().getVehiculo() != null) {
                placa = pago.getMensualidad().getVehiculo().getPlaca();
            }

            addRow(table, "Placa:", placa, boldFont, normalFont);
            addRow(table, "Tipo de Pago:", pago.getTipoPago() != null ? pago.getTipoPago().name() : "-", boldFont, normalFont);
            addRow(table, "Método de Pago:", pago.getMetodoPago() != null ? pago.getMetodoPago().name() : "-", boldFont, normalFont);
            addRow(table, "Valor:", "$" + (pago.getValor() != null ? pago.getValor().toPlainString() : "0"), boldFont, normalFont);
            addRow(table, "Referencia:", pago.getReferencia() != null ? pago.getReferencia() : "-", boldFont, normalFont);
            addRow(table, "Estado:", pago.getEstado() != null ? pago.getEstado().name() : "-", boldFont, normalFont);
            addRow(table, "Registrado por:", pago.getCreadoPor() != null ? pago.getCreadoPor() : "-", boldFont, normalFont);

            document.add(table);

            if (pago.getEstado() == EstadoPago.ANULADO) {
                document.add(Chunk.NEWLINE);
                PdfContentByte canvas = writer.getDirectContent();
                Font watermarkFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 48);
                watermarkFont.setColor(new Color(200, 0, 0));
                Phrase watermark = new Phrase("ANULADO", watermarkFont);
                ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark,
                        document.getPageSize().getWidth() / 2,
                        document.getPageSize().getHeight() / 2, 45);
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    private void addRow(PdfPTable table, String label, String value, Font boldFont, Font normalFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }
}
