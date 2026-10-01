package com.demodd.application.country.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.demodd.application.country.exception.CountryNotFoundApplicationException;
import com.demodd.domain.country.event.CountryDeletedEvent;
import com.demodd.domain.country.model.aggregate.Country;
import com.demodd.domain.country.model.valueobject.CountryId;
import com.demodd.domain.country.port.repository.CountryRepository;

class DeleteCountryUseCaseTest {

    @Test
    void shouldDeleteExistingCountry() {
        Country country = Country.register("Colombia", "CO");
        FakeCountryRepository repository = new FakeCountryRepository(country);
        DeleteCountryUseCase useCase = new DeleteCountryUseCase(repository);

        CountryDeletedEvent event = useCase.execute(country.id());

        assertSame(country, repository.deletedCountry());
        assertEquals(country.id(), event.id());
        assertNotNull(event.occurredOn());
    }

    @Test
    void shouldRejectDeletionWhenCountryDoesNotExist() {
        FakeCountryRepository repository = new FakeCountryRepository(null);
        DeleteCountryUseCase useCase = new DeleteCountryUseCase(repository);

        assertThrows(
                CountryNotFoundApplicationException.class,
                () -> useCase.execute(CountryId.generate())
        );
        assertNull(repository.deletedCountry());
    }

    private static final class FakeCountryRepository implements CountryRepository {
        private final Country country;
        private Country deletedCountry;

        private FakeCountryRepository(Country country) {
            this.country = country;
        }

        @Override
        public Country save(Country country) {
            return country;
        }

        @Override
        public Optional<Country> findById(CountryId id) {
            return Optional.ofNullable(country)
                    .filter(existing -> existing.id().equals(id));
        }

        @Override
        public List<Country> findAll() {
            return country == null ? List.of() : List.of(country);
        }

        @Override
        public boolean existsByCode(String code) {
            return country != null && country.code().equals(code);
        }

        public void delete(Country country) {
            deletedCountry = country;
        }

        private Country deletedCountry() {
            return deletedCountry;
        }
    }
}
