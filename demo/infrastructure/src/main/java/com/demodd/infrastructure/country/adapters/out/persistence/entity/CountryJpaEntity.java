package com.demodd.infrastructure.country.adapters.out.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "countries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_countries_code",
                        columnNames = "code"
                )
        }
)
public class CountryJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false,length = 120)
    private String name;

    @Column(nullable = false,length = 3,unique = true)
    private String code;

    @Column(nullable = false)
    private boolean active;

    public CountryJpaEntity() {
    }

    public CountryJpaEntity(UUID id,String name,String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
