# Gestión del esquema de desarrollo con JPA

## Objetivo

Configurar el perfil `dev` para que Hibernate, mediante Spring Data JPA, mantenga el esquema de PostgreSQL sin ejecutar migraciones de Flyway.

## Alcance

- Cambiar `spring.jpa.hibernate.ddl-auto` de `none` a `update`.
- Permitir que Hibernate cree el namespace configurado cuando todavía no exista.
- Reemplazar la configuración actual de `spring.flyway` por `enabled: false`, ya que las dependencias de Flyway permanecen instaladas y Spring Boot lo habilitaría por defecto si se eliminara toda la sección.
- Conservar la conexión PostgreSQL, el esquema `librarydb_schema`, el dialecto, la estrategia física de nombres y las opciones de registro SQL actuales.

No se modificarán las dependencias Maven ni se eliminarán los archivos de migración. Esto limita el cambio al perfil de desarrollo solicitado.

## Comportamiento esperado

Al iniciar la aplicación con el perfil `dev`, Hibernate se conectará a `librarydb`, creará `librarydb_schema` cuando sea necesario y actualizará sus tablas a partir de las entidades JPA. Flyway estará deshabilitado y no realizará migraciones para este perfil.

`ddl-auto: update` conservará los datos existentes siempre que Hibernate pueda aplicar el cambio de esquema de forma incremental. No sustituye un sistema de migraciones controladas para entornos productivos.

## Verificación

- Comprobar que el YAML resultante sea válido.
- Confirmar que `spring.flyway.enabled` tenga el valor `false` y que no permanezcan otras propiedades de migración en el perfil.
- Confirmar que `ddl-auto` tenga el valor `update` y que esté habilitada la creación de namespaces.
- Ejecutar las pruebas o, si están disponibles, una carga de contexto de Spring para detectar errores de configuración.
