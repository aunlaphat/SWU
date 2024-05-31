package com.arg.swu.digitalsignatures.model.handler;


import com.arg.swu.digitalsignatures.model.receive.DgtKeyStoreUnwrap;

public interface DgtCertificationHandler {
    DgtKeyStoreUnwrap jks() throws Exception;
}
