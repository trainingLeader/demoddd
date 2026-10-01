package com.demodd.domain.common.exception;

import com.demodd.domain.country.model.valueobject.CountryId;

public class CountryNotFoundException
        extends RuntimeException {

    public CountryNotFoundException(
            CountryId id
    ) {
        super(
            "Country not found with id: "
            + id.value()
        );
    }
}
