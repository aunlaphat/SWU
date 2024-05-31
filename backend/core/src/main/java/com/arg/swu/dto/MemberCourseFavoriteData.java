package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberCourseFavoriteData implements Serializable{
	
    private static final long serialVersionUID = 3254125805984268372L;

    @Schema(description = "รหัสอ้างอิง member_info.member_id")
    private Long memberId;

    @Schema(description = "รหัสอ้างอิง coursepublic_id")
    private Long coursepublicId;

}
