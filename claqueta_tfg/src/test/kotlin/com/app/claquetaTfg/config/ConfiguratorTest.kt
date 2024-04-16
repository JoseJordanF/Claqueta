package com.app.claquetaTfg.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class ConfiguratorTest{

    @Test
    fun `When loading the configuration, the HOCON file is the only one that works`() {

        val hoconFilePath = "src/test/resources/configuration/configuracion.conf"

        //When
        Configurator.init("","","","",hoconFilePath)
        println(Configurator.getConfig("DB_USER"))
        val res = Configurator.getConfig("DB_USER")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the Java Properties file is the only one that works`() {

        val propertiesFilePath = "src/test/resources/configuration/configuracion.properties"

        //When
        Configurator.init(propertiesFilePath,"","","","")
        println(Configurator.getConfig("SERVER_PORT"))
        val res = Configurator.getConfig("SERVER_PORT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the JSON file is the only one that works`() {

        val jsonFilePath = "src/test/resources/configuration/configuracion.json"

        //When
        Configurator.init("","",jsonFilePath,"","")
        println(Configurator.getConfig("DB_PASSWORD"))
        val res = Configurator.getConfig("DB_PASSWORD")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the dotenv file is the only one that works`() {

        val envFilePath = "src/test/resources/configuration/configuracion.env"

        //When
        Configurator.init("",envFilePath,"","","")
        println(Configurator.getConfig("VERSION"))
        val res = Configurator.getConfig("VERSION")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When loading the configuration, the YAML file is the only one that works`() {

        val ymlFilePath = "src/test/resources/configuration/configuracion.yml"

        //When
        Configurator.init("","","",ymlFilePath,"")
        println(Configurator.getConfig("SERVER_TIMEOUT"))
        val res = Configurator.getConfig("SERVER_TIMEOUT")

        //Then
        assertNotNull(res)
    }

    @Test
    fun `When the configuration is loaded normally with the default files`() {

        //When
        if (Configurator.init()){
            println("Successfull configuration loading")
        }else{
            println("Unsuccessfull configuration loading")
        }

        val configExample = Configurator.getConfig("DB_PASSWORD")
        println(configExample)

        //Then
        assertFalse { configExample!!.isEmpty()}
    }
}