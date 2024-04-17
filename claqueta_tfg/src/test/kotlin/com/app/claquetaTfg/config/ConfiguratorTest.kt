package com.app.claquetaTfg.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ConfiguratorTest{

    private lateinit var config: Configurator

    @BeforeEach
    fun onBefore() {
        config = Configurator.instance()
    }

    @Test
    fun `When loading the configuration, the HOCON file is the only one that works`() {

        val hoconFilePath =
            "src/test/resources/configuration/config.conf"

        //When
        config = Configurator.instance(hoconFilePath)
        val res = config.getConfig("SERVER_TIMEOUT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the Java Properties file is the only one that works`() {

        val propertiesFilePath =
            "src/test/resources/configuration/config.properties"

        //When
        config = Configurator.instance(propertiesFilePath)
        val res = config.getConfig("SERVER_PORT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the JSON file is the only one that works`() {

        val jsonFilePath =
            "src/test/resources/configuration/config.json"

        //When
        config = Configurator.instance(jsonFilePath)
        val res = config.getConfig("DB_PASSWORD")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the dotenv file is the only one that works`() {

        val envFilePath =
            "src/test/resources/configuration/.env"

        //When
        config = Configurator.instance(envFilePath)
        val res = config.getConfig("VERSION")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the YAML file is the only one that works`() {

        val ymlFilePath =
            "src/test/resources/configuration/config.yml"

        //When
        config = Configurator.instance(ymlFilePath)
        val res = config.getConfig("SERVER_TIMEOUT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When the configuration is loaded normally with the default files`() {

        //When
        val configExample = config.getConfig("DB_PASSWORD")

        //Then
        assertFalse { configExample!!.isEmpty()}
    }
}