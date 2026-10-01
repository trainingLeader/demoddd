# Delete Country Repository Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Añadir al puerto `CountryRepository` la eliminación requerida por `DeleteCountryUseCase` y comprobar sus caminos exitoso y de país inexistente.

**Architecture:** El puerto de dominio declarará `delete(Country)` y la aplicación seguirá orquestando búsqueda, validación y eliminación. Las pruebas usarán un repositorio falso en memoria, sin framework de mocks ni dependencias de infraestructura.

**Tech Stack:** Java 25, Maven, JUnit Jupiter 5.

## Global Constraints

- No introducir Spring ni Mockito en `domain` o `application`.
- No implementar todavía un adaptador de persistencia.
- Mantener sin cambios las firmas públicas de los demás casos de uso.
- Corregir la dependencia de `domain` en `application` para usar `${project.version}`.

---

### Task 1: Probar y completar la eliminación de Country

**Files:**
- Modify: `demo/application/pom.xml`
- Modify: `demo/domain/src/main/java/com/demodd/domain/country/port/repository/CountryRepository.java`
- Create: `demo/application/src/test/java/com/demodd/application/country/usecase/DeleteCountryUseCaseTest.java`

**Interfaces:**
- Consumes: `CountryRepository.findById(CountryId)` y `CountryDeletedEvent`.
- Produces: `CountryRepository.delete(Country)`.

- [ ] **Step 1: Preparar dependencias de compilación y prueba**

Cambiar la versión de `com.demodd:domain` en `demo/application/pom.xml` a `${project.version}` y añadir:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

- [ ] **Step 2: Escribir pruebas fallidas para DeleteCountryUseCase**

Crear `DeleteCountryUseCaseTest` con un `FakeCountryRepository` privado que implemente el contrato actual, mantenga un `Country country` y capture un `Country deletedCountry`.

En el escenario existente, registrar un país, ejecutar el caso de uso y verificar:

```java
assertSame(country, repository.deletedCountry());
assertEquals(country.id(), event.id());
assertNotNull(event.occurredOn());
```

En el escenario inexistente, usar un repositorio vacío y verificar:

```java
assertThrows(CountryNotFoundApplicationException.class,
        () -> useCase.execute(CountryId.generate()));
assertNull(repository.deletedCountry());
```

- [ ] **Step 3: Ejecutar RED**

Ejecutar:

```bash
mvn -f demo/pom.xml test
```

Esperado: fallo de compilación porque `CountryRepository.delete(Country)` todavía no existe.

- [ ] **Step 4: Añadir la operación al puerto**

Añadir a `CountryRepository`:

```java
void delete(Country country);
```

- [ ] **Step 5: Ejecutar GREEN**

Ejecutar:

```bash
mvn -f demo/pom.xml test
```

Esperado: `BUILD SUCCESS`; las pruebas de dominio y aplicación terminan sin fallos.

- [ ] **Step 6: Revisar alcance**

Ejecutar:

```bash
git diff --check
git diff -- demo/application/pom.xml demo/application/src/test demo/domain/src/main/java/com/demodd/domain/country/port/repository/CountryRepository.java
```

Esperado: cambios limitados a la corrección Maven, las pruebas y la nueva operación del puerto.
