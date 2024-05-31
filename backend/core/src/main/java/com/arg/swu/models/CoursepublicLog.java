package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CoursepublicLog extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -1460700943924874570L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coursepublicLogId;
	private String cancelReason;
	private Long coursepublicId;
	private Long coursepublicStatus;
	private Date createTimestamp;
    private Long approverId;

}
