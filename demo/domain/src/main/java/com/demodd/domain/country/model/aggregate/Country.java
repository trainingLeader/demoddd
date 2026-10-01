package com.demodd.domain.country.model.aggregate;

import java.time.LocalDateTime;
import java.util.Objects;

import com.demodd.domain.common.model.AggregateRoot;
import com.demodd.domain.country.event.CountryRegisteredEvent;
import com.demodd.domain.country.event.CountryUpdatedEvent;
import com.demodd.domain.country.model.valueobject.CountryId;

public class Country extends AggregateRoot {
    private final CountryId id;
    private String name;
    private String code;
    private boolean active;

    private Country(
            CountryId id,
            String name,
            String code,
            boolean active) {

        this.id = Objects.requireNonNull(
                id,
                "id must not be null");

        this.name = Objects.requireNonNull(
                name,
                "name must not be null");

        this.code = Objects.requireNonNull(
                code,
                "code must not be null");

        this.active = active;
    }

    public static Country register(
            String name,
            String code) {

        CountryId id = CountryId.generate();

        Country country = new Country(
                id,
                name,
                code,
                true);

        country.recordEvent(
                new CountryRegisteredEvent(
                        id,
                        LocalDateTime.now()));

        return country;
    }

    public static Country restore(
            CountryId id,
            String name,
            String code,
            boolean active) {
        return new Country(
                id,
                name,
                code,
                active);
    }

    public void update(
            String name,
            String code) {

        this.name = Objects.requireNonNull(name);
        this.code = Objects.requireNonNull(code);

        recordEvent(
                new CountryUpdatedEvent(
                        this.id,
                        this.name,
                        this.code,
                        LocalDateTime.now()));
    }

    public CountryId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String code() {
        return code;
    }

    public boolean active() {
        return active;
    }
}
