package com.arg.swu.report;

import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.pdf.PdfTextChunk;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.util.Locale;
import java.util.Map;


public class ThaiJRPdfExporter extends JRPdfExporter {


    @Override
    protected PdfTextChunk getChunk(Map<AttributedCharacterIterator.Attribute, Object> attributes, String text, Locale locale) {
        //PdfTextChunk chunk = this.pdfProducer.createChunk(text, attributes, locale);
        PdfTextChunk chunk = new ThaiClassicPdfProducer(super.createPdfProducerContext()).createChunk(text, attributes, locale);

        if (this.hasUnderline(attributes)) {
            chunk.setUnderline();
        }

        if (this.hasStrikethrough(attributes)) {
            chunk.setStrikethrough();
        }

        Color backcolor = (Color)attributes.get(TextAttribute.BACKGROUND);
        if (backcolor != null) {
            chunk.setBackground(backcolor);
        }

        Object script = attributes.get(TextAttribute.SUPERSCRIPT);
        if (script != null) {
            if (TextAttribute.SUPERSCRIPT_SUPER.equals(script)) {
                chunk.setSuperscript();
            } else if (TextAttribute.SUPERSCRIPT_SUB.equals(script)) {
                chunk.setSubscript();
            }
        }

        return chunk;
    }


}

