package com.arg.swu.provider.handler;

import lombok.Data;

@Data
public abstract class SignProvider {
    // public abstract String path(String fileName);
    public Integer moduleFile;
    public abstract String srcFileBase64() throws Exception;
    public abstract void updateFileResult(byte[] file) throws Exception;
    public abstract void builderData();
}
