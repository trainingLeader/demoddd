package com.demodd.application.country.usecase;

import com.demodd.application.country.dto.CountryResponse;
import com.demodd.application.country.exception.CountryNotFoundApplicationException;
import com.demodd.domain.country.model.valueobject.CountryId;
import com.demodd.domain.country.port.repository.CountryRepository;

public class GetCountryByIdUseCase {

    private final CountryRepository countryRepository;

    public GetCountryByIdUseCase(
            CountryRepository countryRepository
    ) {
        this.countryRepository = countryRepository;
    }

    public CountryResponse execute(
            CountryId id
    ) {

        var country =
                countryRepository.findById(id)
                        .orElseThrow(() ->
                                new CountryNotFoundApplicationException(
                                        id.value().toString()
                                )
                        );

        return new CountryResponse(
                country.id().value(),
                country.name(),
                country.code()
        );
    }
}
