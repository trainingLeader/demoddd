package com.demodd.domain.country.event;

import java.time.LocalDateTime;
import java.util.Objects;

import com.demodd.domain.common.event.DomainEvent;
import com.demodd.domain.country.model.valueobject.CountryId;

public record CountryUpdatedEvent(
        CountryId id,
        String name,
        String code,
        LocalDateTime occurredOn
) implements DomainEvent {

    public CountryUpdatedEvent {

        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(
                occurredOn,
                "occurredOn must not be null"
        );
    }
}
