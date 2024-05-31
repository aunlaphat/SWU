package com.arg.swu.digitalsignatures.service.cert;

import java.util.Date;

public class RevokedCertificateException extends Exception
{
    private static final long serialVersionUID = 3543946618794126654L;
    
    private final Date revocationTime;

    public RevokedCertificateException(String message)
    {
        super(message);
        this.revocationTime = null;
    }

    public RevokedCertificateException(String message, Date revocationTime)
    {
        super(message);
        this.revocationTime = revocationTime;
    }

    public Date getRevocationTime()
    {
        return revocationTime;
    }
}