package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestResetPasswordData implements Serializable {

	private static final long serialVersionUID = -8810280034527578349L;

    @Schema(description = "อีเมล")
	private String memberEmail;

}
