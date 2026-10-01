# JPA Schema Management Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Configurar el perfil `dev` para que Hibernate actualice el esquema PostgreSQL y Flyway no ejecute migraciones.

**Architecture:** Spring Boot conservará el `DataSource` PostgreSQL y delegará la administración incremental de tablas a Hibernate mediante `ddl-auto: update`. Flyway seguirá en el classpath, pero quedará deshabilitado explícitamente para el perfil `dev`.

**Tech Stack:** Java 25, Spring Boot 4.0.2, Spring Data JPA, Hibernate ORM, PostgreSQL, Maven.

## Global Constraints

- Limitar la implementación a `demo/infrastructure/src/main/resources/application-dev.yml`.
- No modificar dependencias Maven ni eliminar migraciones SQL.
- Conservar `librarydb_schema`, el dialecto PostgreSQL, la estrategia física de nombres y el registro SQL actuales.
- Usar `ddl-auto: update` para conservar los datos existentes en desarrollo cuando Hibernate pueda actualizar el esquema incrementalmente.

---

### Task 1: Configurar Hibernate como administrador del esquema de desarrollo

**Files:**
- Modify: `demo/infrastructure/src/main/resources/application-dev.yml:8`
- Test: inspección de configuración y pruebas Maven existentes del reactor `demo`

**Interfaces:**
- Consumes: propiedades de configuración de Spring Boot bajo `spring.jpa` y `spring.flyway`.
- Produces: perfil `dev` con `spring.jpa.hibernate.ddl-auto=update`, `spring.jpa.properties.hibernate.hbm2ddl.create_namespaces=true` y `spring.flyway.enabled=false`.

- [ ] **Step 1: Registrar el estado previo esperado**

Ejecutar:

```bash
rg -n "ddl-auto|flyway|create_namespaces" demo/infrastructure/src/main/resources/application-dev.yml
```

Esperado antes del cambio: aparece `ddl-auto: none`, la sección completa de Flyway y no aparece `create_namespaces`.

- [ ] **Step 2: Aplicar la configuración mínima**

En `application-dev.yml`, cambiar el bloque relevante para que contenga:

```yaml
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        "[physical_naming_strategy]": org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        "[default_schema]": librarydb_schema
        "[format_sql]": true
        "[hbm2ddl.create_namespaces]": true

  flyway:
    enabled: false
```

Eliminar de `spring.flyway` las propiedades de locations, schemas, initialization, history table, baseline y placeholders.

- [ ] **Step 3: Inspeccionar el YAML resultante**

Ejecutar:

```bash
sed -n '1,80p' demo/infrastructure/src/main/resources/application-dev.yml
rg -n "ddl-auto: update|enabled: false|hbm2ddl.create_namespaces" demo/infrastructure/src/main/resources/application-dev.yml
```

Esperado: las tres propiedades nuevas aparecen una vez; la conexión PostgreSQL y el resto de las opciones JPA permanecen sin cambios.

- [ ] **Step 4: Verificar el proyecto con Maven**

Ejecutar:

```bash
mvn -f demo/pom.xml test
```

Esperado: `BUILD SUCCESS`. Si no existen pruebas que inicien el perfil `dev`, esta ejecución valida al menos el reactor y la compilación; la inspección del paso anterior cubre las propiedades específicas del perfil.

- [ ] **Step 5: Revisar el diff**

Ejecutar:

```bash
git diff --check
git diff -- demo/infrastructure/src/main/resources/application-dev.yml
```

Esperado: no hay errores de espacios y el diff solo contiene el cambio aprobado del YAML.
