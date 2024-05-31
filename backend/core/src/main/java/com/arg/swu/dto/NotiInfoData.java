/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class NotiInfoData extends PageableCommon implements Serializable{
    private Long notiId;
    private Long notiTopic;
    private Long notiRecipient;
    private String notiDetail;
    private Boolean activeFlag;
    private Date createDate;
    private Long createById;
    private Date updateDate;
    private Long updateById;
    private String topicEn;
    private String topicTh;
    private String recipientEn;
    private String recipientTh;
}
