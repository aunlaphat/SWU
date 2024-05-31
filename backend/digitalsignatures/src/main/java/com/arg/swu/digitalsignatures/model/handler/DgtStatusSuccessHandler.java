package com.arg.swu.digitalsignatures.model.handler;

public interface DgtStatusSuccessHandler {
    void onStatus(Long byId, String status, String message);
}
