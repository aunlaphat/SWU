package com.arg.swu.report;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.SplitCharacter;
import net.sf.jasperreports.export.pdf.*;
import net.sf.jasperreports.export.pdf.classic.ClassicPdfProducer;
import net.sf.jasperreports.export.pdf.classic.ClassicTextChunk;

import java.text.AttributedCharacterIterator;
import java.util.Locale;
import java.util.Map;

public class ThaiClassicPdfProducer extends ClassicPdfProducer {

    private SplitCharacter splitCharacter;
    public ThaiClassicPdfProducer(PdfProducerContext context) {
        super(context);
    }

    @Override
    public PdfTextChunk createChunk(String text, Map<AttributedCharacterIterator.Attribute,Object> attributes, Locale locale) {
        Font font = getFont(attributes, locale);
        //Chunk chunk = new Chunk(text, font);
        Chunk chunk = new ThaiChunk( new Chunk(text, font));
        if (splitCharacter != null)
        {
            //TODO use line break offsets if available?
            chunk.setSplitCharacter(splitCharacter);
        }

        return new ClassicTextChunk(this, chunk, font);
    }
}
