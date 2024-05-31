package com.arg.swu.digitalsignatures.util;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class KeystoreUtils {

    byte[] rawdata;
    public KeystoreUtils loadFile(String file) throws IOException, URISyntaxException
    {
        rawdata = Files.readAllBytes(Paths.get(getClass().getResource(file).toURI()));
        return this;
    }

    public KeystoreUtils loadFromResource(String file) throws IOException, URISyntaxException
    {
        rawdata = Files.readAllBytes(Paths.get(getClass().getResource(file).toURI()));
        return this;
    }

    public String generateToBase64() throws IOException, URISyntaxException
    {
        return Base64.getEncoder().encodeToString(this.rawdata);
    }

    public byte[] generateBase64ToByteArray(String data) throws IOException, URISyntaxException
    {
        
        return Base64.getDecoder().decode(data);
    }
    public byte[] generateBase64ToByteArray() throws IOException, URISyntaxException
    {
        
        return Base64.getDecoder().decode(this.rawdata);
    }

}
