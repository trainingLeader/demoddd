package com.demodd.application.country.dto;

import java.util.UUID;

public record CountryResponse(
        UUID id,
        String name,
        String code
) {
}
