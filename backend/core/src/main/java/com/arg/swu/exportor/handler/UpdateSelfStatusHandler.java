package com.arg.swu.exportor.handler;

public abstract class UpdateSelfStatusHandler<K> {
    public abstract void updateStatus(Long entryId, K status, String msg);
}
