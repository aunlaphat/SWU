package com.arg.swu.models.lookup;

import com.arg.swu.models.commons.LookupCatalog;
import com.arg.swu.models.commons.LookupCatalogInstant;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue(LookupCatalogInstant.INDUSTRY_GROUP)
@Data
public class IndustryGroupLookup extends LookupCatalog{

}
