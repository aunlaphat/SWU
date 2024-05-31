package com.arg.swu.models.coursemgt;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CourseMgtPosDuty extends Footprint{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseMgtTeachModelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseMgtId", referencedColumnName = "courseId", nullable = true
    , foreignKey=@ForeignKey(name="coursemgtposduty_fk_lookup_coursemgt"))
    private CourseMgt courseMgt;

    private String pdName;
    private String pdLevel;
    private String pdTel;
    private String pdEmail;
}
