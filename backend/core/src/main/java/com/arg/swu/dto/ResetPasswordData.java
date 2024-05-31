package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResetPasswordData implements Serializable {

	private static final long serialVersionUID = -991027003352847724L;
    
    @Schema(description = "new Password")
    private String newPassword;

    @Schema(description = "confirm New Password")
    private String confirmNewPassword;

    @Schema(description = "token")
	private String token;

}
