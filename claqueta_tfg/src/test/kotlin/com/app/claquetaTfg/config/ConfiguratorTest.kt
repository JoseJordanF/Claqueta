package com.app.claquetaTfg.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ConfiguratorTest {

    private lateinit var config: Configurator

    @BeforeEach
    fun onBefore() {
        config = Configurator.instance()
    }

    @Test
    fun `We load the configuration and it is loaded from system properties`() {

        System.setProperty("CLAQUETA_DB_USER", "user")
        System.setProperty("CLAQUETA_VERSION", "1.2")

        //When
        config = Configurator.instance()
        val res = listOf(config.getConfig("CLAQUETA_DB_USER"), config.getConfig("CLAQUETA_VERSION"))

        //Then
        assertTrue(res.isNotEmpty())
    }

    @Test
    fun `We load the configuration and it is loaded from the system environment variables`() {

        //Then
        assertThrows<RuntimeException> {
            //The assertion is intended to fail because in the repository we will not have those system environment variables.
            val configExample = config.getConfig("CLAQUETA_DB")
        }
    }

    @Test
    fun `We load the configuration and it will be loaded from the configuration file that we specify`() {

        val file = "src/test/resources/configuration/config.json"

        //when
        config = Configurator.instance(file)
        val res = config.getConfig("CLAQUETA_SERVER_PORT")

        //Then
        assertNotNull(res)
    }

}