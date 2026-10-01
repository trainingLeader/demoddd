package com.demodd.infrastructure.country.adapters.out.persistence.mappers;

import com.demodd.domain.country.model.aggregate.Country;
import com.demodd.domain.country.model.valueobject.CountryId;
import com.demodd.infrastructure.country.adapters.out.persistence.entity.CountryJpaEntity;

public class CountryPersistenceMapper {

    public CountryJpaEntity toJpa(Country domain) {

        if (domain == null) {
            return null;
        }

        CountryJpaEntity jpa = new CountryJpaEntity();

        jpa.setId(domain.id().value());
        jpa.setName(domain.name());
        jpa.setCode(domain.code());
        jpa.setActive(domain.active());

        return jpa;
    }

    public Country toDomain(CountryJpaEntity jpa) {

        if (jpa == null) {
            return null;
        }

        return Country.restore(
                new CountryId(jpa.getId()),
                jpa.getName(),
                jpa.getCode(),
                jpa.isActive()
        );
    }
}