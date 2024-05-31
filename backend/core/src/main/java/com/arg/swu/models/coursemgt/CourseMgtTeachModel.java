package com.arg.swu.models.coursemgt;

import com.arg.swu.models.commons.Footprint;
import com.arg.swu.models.lookup.PatternType;
import com.arg.swu.models.lookup.TimeUnitType;

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
public class CourseMgtTeachModel extends Footprint
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseMgtTeachModelId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courseMgtId", referencedColumnName = "courseId", nullable = true
    , foreignKey=@ForeignKey(name="course_fk_lookup_coursemgt"))
    private CourseMgt courseMgt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tmPatternTypeId", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="course_fk_lookup_tmpatterntype"))
    private PatternType tmPatternType;
    
    private String tmTheoryHourAmt;
    private String tmActionHourAmt;
    private String tmTotalHourAmt;
    private String tmCourseDurationAmt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tmTimeUnitTypeId", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="course_fk_lookup_tmtimeunittype"))
    private TimeUnitType tmTimeUnitType;

    private String tmOpenOrderAmt;
}
