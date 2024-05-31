package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MemberCourseFavorite extends Footprint implements Serializable {

    private static final long serialVersionUID = 375246100582992632L;

    @Id
    private Long memberId;
    private Long coursepublicId;
    
}