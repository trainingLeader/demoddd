package com.demodd.application.country.command;

import java.util.Objects;

import com.demodd.domain.country.model.valueobject.CountryId;

public record UpdateCountryCommand(
        CountryId id,
        String name,
        String code
) {

    public UpdateCountryCommand {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(code, "code must not be null");
    }
}
