package com.arg.swu.mailgateway.order;
 

import com.arg.swu.mailgateway.model.MailTemplate;
import com.arg.swu.mailgateway.model.MailTenantProvider;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class RequestRegisterTemplate {
    private MailTenantProvider mailTenantProvider;
    private List<MailTemplate> mailTemplate;
}
