package com.demodd.domain.country.model.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.demodd.domain.country.event.CountryRegisteredEvent;

class CountryTest {

    @Test
    void shouldRegisterCountryCreatedEvent() {
        Country country = Country.register("Colombia", "CO");

        assertEquals(1, country.domainEvents().size());
        CountryRegisteredEvent event = assertInstanceOf(
                CountryRegisteredEvent.class,
                country.domainEvents().getFirst()
        );
        assertEquals(country.id(), event.id());
        assertNotNull(event.occurredOn());
    }
}
