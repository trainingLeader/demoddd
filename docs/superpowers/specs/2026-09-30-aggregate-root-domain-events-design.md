# Registro de eventos en AggregateRoot

## Objetivo

Corregir el error de compilación de `Country` e introducir una abstracción reutilizable para que los agregados de dominio registren eventos sin depender de Spring ni de infraestructura.

## Diseño

Se creará `com.demodd.domain.common.model.AggregateRoot` como clase abstracta. Mantendrá una colección privada de `DomainEvent` y ofrecerá estas operaciones:

- `protected final void recordEvent(DomainEvent event)`: valida que el evento no sea nulo y lo registra.
- `public final List<DomainEvent> domainEvents()`: devuelve una copia inmutable de los eventos pendientes.
- `public final void clearDomainEvents()`: elimina los eventos pendientes después de que un publicador externo los procese.

`Country` extenderá `AggregateRoot`. Su fábrica `register()` conservará la llamada existente a `recordEvent(new CountryRegisteredEvent(...))`.

## Límites

- No se modificarán `DomainEvent` ni `CountryRegisteredEvent`.
- No se añadirá una dependencia de Spring al módulo de dominio.
- No se implementará todavía un publicador de eventos; queda fuera del error actual.
- Se añadirá únicamente JUnit Jupiter con alcance `test` al `pom.xml` de `domain`, porque ese módulo no tiene actualmente una biblioteca de pruebas.

## Verificación

Una prueba de `Country.register()` comprobará que se registra exactamente un `CountryRegisteredEvent`, que contiene el mismo `CountryId` que el agregado y que su fecha no es nula. Otra prueba comprobará que `clearDomainEvents()` vacía la colección. La colección expuesta debe ser inmutable y `recordEvent(null)` debe rechazarse mediante `NullPointerException`.

La verificación se ejecutará directamente sobre `demo/domain/pom.xml`, evitando el fallo preexistente de versión entre los módulos `application` y `domain` en el reactor completo.
