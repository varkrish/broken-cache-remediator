# Broken Caching Region Factory

The `BrokenCachingRegionFactory` is a custom implementation of Hibernate's `RegionFactoryTemplate` that uses Infinispan as a cache provider. This document provides guidance on how to configure and use this region factory in your Hibernate application.

## Prerequisites

- Java 17 or later
- Maven for dependency management
- Hibernate ORM
- Infinispan

## Dependencies

Ensure you have the necessary dependencies in your `pom.xml`. Here's a basic setup:

```xml
<dependencies>
    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>${hibernate.version}</version>
    </dependency>

    <!-- Infinispan Core -->
    <dependency>
        <groupId>org.infinispan</groupId>
        <artifactId>infinispan-core</artifactId>
        <version>${infinispan.version}</version>
    </dependency>

    <!-- Byte Buddy (for bytecode manipulation) -->
    <dependency>
        <groupId>net.bytebuddy</groupId>
        <artifactId>byte-buddy</artifactId>
        <version>1.12.8</version> <!-- Use the latest stable version -->
    </dependency>

    <!-- JUnit for Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.3</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Configuration

To use `BrokenCachingRegionFactory`, you need to configure Hibernate to use it as the cache region factory. Update your Hibernate configuration file (e.g., `hibernate.cfg.xml` or `application.properties` for Spring Boot) with the following settings:

### `hibernate.cfg.xml`

```xml
<hibernate-configuration>
    <session-factory>
        <!-- Other Hibernate configurations -->

        <!-- Cache region factory -->
        <property name="hibernate.cache.region.factory_class">
            org.broken.cacheremidator.BrokenCachingRegionFactory
        </property>

        <!-- Cache provider configuration -->
        <property name="hibernate.cache.use_second_level_cache">true</property>
        <property name="hibernate.cache.use_query_cache">true</property>
    </session-factory>
</hibernate-configuration>
```

### `application.properties` (Spring Boot)

```properties
spring.jpa.properties.hibernate.cache.region.factory_class=org.broken.cacheremidator.BrokenCachingRegionFactory
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
```

## Usage

To use the `BrokenCachingRegionFactory`, simply include it in your Hibernate configuration as shown above. The custom region factory will handle caching using Infinispan and provide the necessary cache management.

## Running Tests

You can run unit tests for the `BrokenCachingRegionFactory` using Maven:

```bash
mvn test
```

Ensure that all required dependencies are included and properly configured to avoid runtime issues.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
