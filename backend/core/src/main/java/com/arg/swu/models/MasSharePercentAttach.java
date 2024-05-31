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
public class MasSharePercentAttach extends Footprint implements Serializable {

	private static final long serialVersionUID = -8839646841904747357L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sharePercentAttachId;
    private Long depId;
    private String fileName;
    private String file_link;
}
