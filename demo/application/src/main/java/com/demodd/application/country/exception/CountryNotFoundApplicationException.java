package com.demodd.application.country.exception;

import com.demodd.application.common.ApplicationException;

public class CountryNotFoundApplicationException
        extends ApplicationException {

    public CountryNotFoundApplicationException(String message) {
        super(message);
    }
}
