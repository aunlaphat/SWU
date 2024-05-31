package com.arg.swu.models.coursemgt;

import com.arg.swu.models.commons.Footprint;
import com.arg.swu.models.lookup.GradeConceptType;
import com.arg.swu.models.lookup.GradeModelType;

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
public class CourseMgtGrade extends Footprint{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseMgtGradeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseMgtId", referencedColumnName = "courseId", nullable = true
    , foreignKey=@ForeignKey(name="coursemgtgrade_fk_lookup_coursemgt"))
    private CourseMgt courseMgt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gradeModelTypeId", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="coursemgtgrade_fk_lookup_grademodeltype"))
    private GradeModelType gradeModelType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gmConceptTypeId", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="coursemgtgrade_fk_lookup_gmconcepttype"))
    private GradeConceptType gmConceptType;
    
    private Integer gmUnitAmt;
    
}
