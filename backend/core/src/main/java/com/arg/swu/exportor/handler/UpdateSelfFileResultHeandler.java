package com.arg.swu.exportor.handler;

import com.arg.swu.models.commons.Footprint;

public abstract class UpdateSelfFileResultHeandler<K, F> {
    public abstract void updateFileResult(Long entryId, Float fileProgress,K fileStatus, String msg, F fileModel, Footprint footprint);
}
