# Eliminación de Country mediante el puerto de repositorio

## Objetivo

Corregir el error de compilación de `DeleteCountryUseCase` incorporando al puerto de dominio la operación de eliminación que el caso de uso necesita y verificando el flujo con pruebas unitarias.

## Diseño

`CountryRepository` declarará `void delete(Country country)`. El caso de uso conservará su flujo actual: buscar por `CountryId`, lanzar `CountryNotFoundApplicationException` cuando no exista, eliminar el agregado encontrado y devolver un `CountryDeletedEvent` con el identificador solicitado.

Las pruebas utilizarán una implementación falsa de `CountryRepository` dentro del código de prueba, sin Mockito ni Spring. Se comprobarán dos escenarios:

- Un país existente se entrega al método `delete` y el evento resultante contiene su identificador y una fecha no nula.
- Un identificador inexistente produce `CountryNotFoundApplicationException` y no intenta eliminar ningún agregado.

## Ajustes de compilación necesarios

El `pom.xml` de `application` referencia por error `domain:1.0.0-SNAPSHOT`, mientras ambos módulos tienen versión `1.0-SNAPSHOT`. Se cambiará esa dependencia a `${project.version}` para que el reactor resuelva el módulo correcto. También se añadirá `junit-jupiter` con alcance `test` para ejecutar las pruebas, sin introducir Spring en la capa de aplicación.

## Límites

- No se implementará todavía un adaptador de persistencia; cuando exista, deberá implementar `delete(Country)`.
- No se modificará la API pública de los demás casos de uso.
- No se cambiará la forma en que `DeleteCountryUseCase` construye `CountryDeletedEvent`.

## Verificación

Se ejecutará `mvn -f demo/pom.xml test` para compilar los módulos en orden y comprobar las pruebas de dominio y aplicación. También se verificará el diff para confirmar que los cambios se limitan al puerto, las pruebas y las dependencias Maven necesarias.
