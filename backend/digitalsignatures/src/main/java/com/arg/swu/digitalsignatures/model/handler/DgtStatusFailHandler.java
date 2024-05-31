package com.arg.swu.digitalsignatures.model.handler;

public interface DgtStatusFailHandler {
    void onStatus(Long byId, String status, String message);
}
