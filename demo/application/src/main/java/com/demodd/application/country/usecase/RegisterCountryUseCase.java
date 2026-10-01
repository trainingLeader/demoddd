package com.demodd.application.country.usecase;

import com.demodd.application.country.command.RegisterCountryCommand;
import com.demodd.application.country.dto.CountryResponse;
import com.demodd.domain.country.model.aggregate.Country;
import com.demodd.domain.country.port.repository.CountryRepository;

public class RegisterCountryUseCase {

    private final CountryRepository countryRepository;

    public RegisterCountryUseCase(
            CountryRepository countryRepository
    ) {
        this.countryRepository = countryRepository;
    }

    public CountryResponse execute(
            RegisterCountryCommand command
    ) {

        Country country = Country.register(
                command.name(),
                command.code()
        );

        Country saved =
                countryRepository.save(country);

        return new CountryResponse(
                saved.id().value(),
                saved.name(),
                saved.code()
        );
    }
}
