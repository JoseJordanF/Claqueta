package com.app.claquetaTfg.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ConfiguratorTest{

    @BeforeEach
    fun onBefore() {
        Configurator.init()
    }

    @Test
    fun `When I want to load the configuration of a HOCON file`() {

        val hoconFilePath =
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.conf"

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
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.properties"

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
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.json"

        //When
        Configurator.loadConfig(josnFilePath)
        println(Configurator.getConfig("database.credentials.username"))
        val res = Configurator.getConfig("database.credentials.username")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When I want to load all settings`() {

        val hoconFilePath =
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.conf"
        val propertiesFilePath =
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.properties"
        val josnFilePath =
            "C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\configuracion.json"


        //When
        Configurator.loadConfig(hoconFilePath)
        Configurator.loadConfig(propertiesFilePath)
        Configurator.loadConfig(josnFilePath)

        val configList = listOf<String>(
            Configurator.getConfig("database.options.timeout"),
            Configurator.getConfig("url.database"), Configurator.getConfig("database.credentials.password"),
        )
        println(configList[0])
        println(configList[1])
        println(configList[2])

        //Then
        assertFalse { configList.any { it.isEmpty() } }
    }

}