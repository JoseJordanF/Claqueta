package com.app.claquetaTfg.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ConfiguratorTest{

    @BeforeEach
    fun onBefore() {
        Configurator.init()
    }

    @Test
    fun `When I want to load the configuration of an env file`() {

        val envFilePath =
            "src/test/resources/configuracion.env"

        //When
        Configurator.loadConfig(envFilePath)
        println(Configurator.getConfig("DB_PORT"))
        val res = Configurator.getConfig("DB_PORT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load the configuration of a HOCON file`() {

        val hoconFilePath =
            "src/test/resources/configuracion.conf"

        //When
        Configurator.loadConfig(hoconFilePath)
        println(Configurator.getConfig("database.options.timeout"))
        val res = Configurator.getConfig("database.options.timeout")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load the configuration of a properties file`() {

        val propertiesFilePath =
            "src/test/resources/configuracion.properties"

        //When
        Configurator.loadConfig(propertiesFilePath)
        println(Configurator.getConfig("url.database"))
        val res = Configurator.getConfig("url.database")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load the configuration of a JSON file`() {

        val josnFilePath =
            "src/test/resources/Configuracion.json"

        //When
        Configurator.loadConfig(josnFilePath)
        println(Configurator.getConfig("database.credentials.username"))
        val res = Configurator.getConfig("database.credentials.username")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load the configuration from inline text strings`() {

        val configString = """
        database {
          host = "localhost"
          port = 5432
          credentials {
            username = "myuser"
            password = "mypassword"
          }
          options {
            timeout = 30
            max_connections = 100
          }
        }
    """.trimIndent()

        //When
        Configurator.loadConfig(configString)
        println(Configurator.getConfig("database.options.max_connections"))
        val res = Configurator.getConfig("database.options.max_connections")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load the string configuration inline, but it is not an acceptable string`() {

        //When
        val configString = "String no aceptable para ser procesado".trimIndent()

        //Then
        assertThrows<RuntimeException> {
            Configurator.loadConfig(configString)
            println(Configurator.getConfig("database.options.max_connections"))
        }
    }

    @Test
    fun `When I want to load all settings`() {

        val envFilePath =
            "src/test/resources/configuracion.env"
        val hoconFilePath =
            "src/test/resources/configuracion.conf"
        val propertiesFilePath =
            "src/test/resources/configuracion.properties"
        val josnFilePath =
            "src/test/resources/configuracion.json"
        val configString = """
        database {
          host = "localhost"
          port = 5432
          credentials {
            username = "myuser"
            password = "mypassword"
          }
          options {
            timeout = 30
            max_connections = 100
          }
        }
        """.trimIndent()

        //When
        Configurator.loadConfig(envFilePath)
        Configurator.loadConfig(hoconFilePath)
        Configurator.loadConfig(propertiesFilePath)
        Configurator.loadConfig(josnFilePath)
        Configurator.loadConfig(configString)

        val configList = listOf<String>(
            Configurator.getConfig("DB_PORT"), Configurator.getConfig("database.options.timeout"),
            Configurator.getConfig("url.database"), Configurator.getConfig("database.credentials.username"),
            Configurator.getConfig("database.options.max_connections")
        )

        println(configList[0])
        println(configList[1])
        println(configList[2])
        println(configList[3])
        println(configList[4])

        //Then
        assertFalse { configList.any { it.isEmpty() } }
    }

}