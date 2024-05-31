package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TotalRegisterData implements Serializable {

	private static final long serialVersionUID = 7542224345124561365L;

    @Schema(description = "Coursepublic Id")
    private Long coursepublicId;

    @Schema(description = "Total Register")
    private Long totalRegister;

}
