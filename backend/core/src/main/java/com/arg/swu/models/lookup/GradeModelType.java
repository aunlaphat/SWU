package com.arg.swu.models.lookup;

import com.arg.swu.models.commons.LookupCatalog;
import com.arg.swu.models.commons.LookupCatalogInstant;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue(LookupCatalogInstant.GRADE_MODEL_TYPE)
@Data
public class GradeModelType extends LookupCatalog{

}
