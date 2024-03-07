package com.mavi.memorize

import org.junit.jupiter.api.extension.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer


@SpringBootTest
@ExtendWith(DatabaseContainerExtension::class)
annotation class DBTest

class DatabaseContainerExtension : BeforeAllCallback, AfterAllCallback, AfterEachCallback {
    private val postgresContainer = PostgreSQLContainer("postgres:16-alpine").withReuse(true)
    override fun beforeAll(context: ExtensionContext?) {
        postgresContainer.start()
        System.setProperty(
            "spring.datasource.url",
            "jdbc:postgresql://${postgresContainer.host}:${postgresContainer.getMappedPort(5432)}/${postgresContainer.databaseName}"
        )
        System.setProperty("spring.datasource.username", postgresContainer.username)
        System.setProperty("spring.datasource.password", postgresContainer.password)
    }

    override fun afterAll(context: ExtensionContext?) {
        postgresContainer.stop()
    }

    override fun afterEach(context: ExtensionContext) {
        SpringExtension.getApplicationContext(context)
            .getBeansOfType(JpaRepository::class.java).values.forEach {
                it.deleteAllInBatch()
                it.flush()
            }
    }
}
