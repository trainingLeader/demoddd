package com.demodd.infrastructure.country.adapters.in.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCountryRequest(

        @NotBlank(message = "name is required")
        String name,

        @NotBlank(message = "code is required")
        @Size(min = 2, max = 3, message = "code must have between 2 and 3 characters")
        String code

) {
}
