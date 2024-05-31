package com.arg.swu.mailgateway.order;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RequestOrderMail {
    private Long tennantId;
    private Long templateId;

    private String sendToMail;
    private String sendFromMail;
    private String sendFromFullName;
    private String dearTopic;
    private String subject;
    private String ccMail;
    private String templateFileName;


    private Map<String, Object> bodyParam;
}
