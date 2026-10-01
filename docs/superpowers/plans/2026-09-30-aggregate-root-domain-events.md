# Aggregate Root Domain Events Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Corregir la compilación de `Country` mediante una abstracción de agregado que registre y exponga eventos de dominio pendientes.

**Architecture:** El módulo puro `domain` tendrá una clase base abstracta `AggregateRoot` que poseerá la colección de eventos. `Country` heredará de ella y continuará registrando `CountryRegisteredEvent` desde su fábrica; consumidores futuros podrán leer y limpiar los eventos sin mutar directamente la colección.

**Tech Stack:** Java 25, Maven, JUnit Jupiter 5, API de colecciones de Java.

## Global Constraints

- No introducir dependencias de Spring en `domain`.
- No modificar `DomainEvent` ni `CountryRegisteredEvent`.
- No implementar publicación de eventos fuera del agregado.
- Ejecutar las pruebas con `mvn -f demo/domain/pom.xml test` porque el reactor completo tiene un fallo preexistente de versiones entre módulos.

---

### Task 1: Implementar registro de eventos en AggregateRoot

**Files:**
- Create: `demo/domain/src/main/java/com/demodd/domain/common/model/AggregateRoot.java`
- Modify: `demo/domain/src/main/java/com/demodd/domain/country/model/aggregate/Country.java`
- Modify: `demo/domain/pom.xml`
- Create: `demo/domain/src/test/java/com/demodd/domain/common/model/AggregateRootTest.java`
- Create: `demo/domain/src/test/java/com/demodd/domain/country/model/aggregate/CountryTest.java`

**Interfaces:**
- Consumes: `com.demodd.domain.common.event.DomainEvent`.
- Produces: `AggregateRoot.recordEvent(DomainEvent)`, `AggregateRoot.domainEvents()` y `AggregateRoot.clearDomainEvents()`.
- Produces: `Country` como subtipo de `AggregateRoot`.

- [ ] **Step 1: Añadir JUnit Jupiter para las pruebas del módulo**

Añadir a `demo/domain/pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

- [ ] **Step 2: Escribir las pruebas fallidas de AggregateRoot**

Crear `AggregateRootTest` con un subtipo privado que exponga una operación `record(DomainEvent)`. Verificar tres comportamientos independientes:

```java
assertEquals(List.of(event), aggregate.domainEvents());
assertThrows(UnsupportedOperationException.class,
        () -> aggregate.domainEvents().clear());
assertThrows(NullPointerException.class, () -> aggregate.record(null));
```

Después de registrar un evento y llamar `clearDomainEvents()`, verificar:

```java
assertTrue(aggregate.domainEvents().isEmpty());
```

Usar un `TestDomainEvent` local con `LocalDateTime occurredOn` para evitar dependencias en el agregado `Country`.

- [ ] **Step 3: Escribir la prueba fallida de Country.register()**

Crear `CountryTest` y verificar:

```java
Country country = Country.register("Colombia", "CO");
DomainEvent event = assertInstanceOf(
        CountryRegisteredEvent.class,
        assertDoesNotThrow(() -> country.domainEvents().getFirst())
);
CountryRegisteredEvent registeredEvent = (CountryRegisteredEvent) event;

assertEquals(1, country.domainEvents().size());
assertEquals(country.id(), registeredEvent.id());
assertNotNull(registeredEvent.occurredOn());
```

- [ ] **Step 4: Ejecutar las pruebas para comprobar RED**

Ejecutar:

```bash
mvn -f demo/domain/pom.xml test
```

Esperado: fallo de compilación porque `AggregateRoot` y sus métodos todavía no existen, además de la llamada actualmente no resuelta a `Country.recordEvent(...)`.

- [ ] **Step 5: Implementar AggregateRoot mínimamente**

Crear la clase abstracta con esta estructura:

```java
public abstract class AggregateRoot {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected final void recordEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "event must not be null"));
    }

    public final List<DomainEvent> domainEvents() {
        return List.copyOf(domainEvents);
    }

    public final void clearDomainEvents() {
        domainEvents.clear();
    }
}
```

Importar `ArrayList`, `List`, `Objects` y `DomainEvent`.

- [ ] **Step 6: Conectar Country con AggregateRoot**

Importar `com.demodd.domain.common.model.AggregateRoot` y cambiar la declaración a:

```java
public class Country extends AggregateRoot {
```

No alterar la fábrica ni los atributos de `Country`.

- [ ] **Step 7: Ejecutar las pruebas para comprobar GREEN**

Ejecutar:

```bash
mvn -f demo/domain/pom.xml test
```

Esperado: `BUILD SUCCESS`, con todas las pruebas nuevas aprobadas y sin fallos.

- [ ] **Step 8: Revisar alcance y formato**

Ejecutar:

```bash
git diff --check
git diff -- demo/domain/pom.xml demo/domain/src/main demo/domain/src/test
```

Esperado: ningún error de formato y cambios limitados a la clase base, la herencia de `Country`, la dependencia de prueba y las pruebas descritas.
