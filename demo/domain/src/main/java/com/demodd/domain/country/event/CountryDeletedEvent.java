package com.demodd.domain.country.event;

import java.time.LocalDateTime;
import java.util.Objects;

import com.demodd.domain.common.event.DomainEvent;
import com.demodd.domain.country.model.valueobject.CountryId;

public record CountryDeletedEvent(
        CountryId id,
        LocalDateTime occurredOn
) implements DomainEvent {

    public CountryDeletedEvent {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(occurredOn, "occurredOn must not be null");
    }
}
