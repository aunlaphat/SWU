/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
@Entity
public class NewsInfo extends Footprint implements Serializable {
    private static final long serialVersionUID = -3816589379391093546L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;
    private String newsHeading;
    private Long newsFormat;
    private Date newsStart;
    private Date newsEnd;
    private Boolean newsStatus;
    private Long coursepublicRefId;
    private String newsCoverimage;
    private String newsDetail;
    private Long newsOrder;
    private Long newsHilight;
    private Boolean newFlag;
    private Date newIconStart;
    private Date newIconEnd;
    private String filename;
    private String prefix;
    private Long module;
}
