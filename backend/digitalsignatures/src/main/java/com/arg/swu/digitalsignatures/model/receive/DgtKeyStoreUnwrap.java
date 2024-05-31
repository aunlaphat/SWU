package com.arg.swu.digitalsignatures.model.receive;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DgtKeyStoreUnwrap {
    private KeyStore keyStore;
    private PrivateKey privateKey;
    private Certificate [] chain;
    private char[] pin;
}
