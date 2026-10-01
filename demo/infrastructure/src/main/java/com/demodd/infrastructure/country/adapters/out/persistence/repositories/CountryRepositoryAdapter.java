package com.demodd.infrastructure.country.adapters.out.persistence.repositories;

import java.util.List;
import java.util.Optional;

import com.demodd.domain.country.model.aggregate.Country;
import com.demodd.domain.country.model.valueobject.CountryId;
import com.demodd.domain.country.port.repository.CountryRepository;
import com.demodd.infrastructure.country.adapters.out.persistence.entity.CountryJpaEntity;
import com.demodd.infrastructure.country.adapters.out.persistence.mappers.CountryPersistenceMapper;

public class CountryRepositoryAdapter implements CountryRepository {

    private final CountryJpaRepository countryJpaRepository;
    private final CountryPersistenceMapper mapper;

    public CountryRepositoryAdapter(
            CountryJpaRepository countryJpaRepository,
            CountryPersistenceMapper mapper
    ) {
        this.countryJpaRepository = countryJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Country save(Country country) {

        CountryJpaEntity entity =
                mapper.toJpa(country);

        CountryJpaEntity saved =
                countryJpaRepository.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Country> findById(
            CountryId id
    ) {

        return countryJpaRepository.findById(
                        id.value()
                )
                .map(mapper::toDomain);
    }

    @Override
    public List<Country> findAll() {

        return countryJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCode(
            String code
    ) {
        return countryJpaRepository.existsByCode(code);
    }

    @Override
    public void delete(Country country) {

        countryJpaRepository.deleteById(
                country.id().value()
        );
    }
}
