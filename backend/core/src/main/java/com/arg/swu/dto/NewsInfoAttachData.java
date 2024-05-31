/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class NewsInfoAttachData extends PageableCommon implements Serializable{
    private Long newsAttachId;
    private Long newsId;
    private String fileNameTh;
    private String fileNameEn;
    private String filename;
    private String prefix;
    private Long module;
    private String type;

    private String base64;
}
