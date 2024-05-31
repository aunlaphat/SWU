package com.arg.swu.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Charge3dMpi implements Serializable {
	
	private static final long serialVersionUID = -6079866104877125820L;
	
	private String eci;
	private String xid;
	private String cavv;
	
	@SerializedName("kbank_mpi")
	private String kbankMpi;

}
