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
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
@Entity
public class NewsInfoAttach extends Footprint implements Serializable {
	
    private static final long serialVersionUID = -3816580469391093546L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsAttachId;
    private Long newsId;
    private String fileNameTh;
    private String fileNameEn;
    private String filename;
    private String prefix;
    private Long module;
    private String type;
}
