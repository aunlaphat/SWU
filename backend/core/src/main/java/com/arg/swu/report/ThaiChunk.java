package com.arg.swu.report;

import com.lowagie.text.Chunk;

public class ThaiChunk extends Chunk {
    public ThaiChunk(Chunk var1) {
        super(var1);
        ThaiDisplayUtils.toDisplayString(content);
    }
}
