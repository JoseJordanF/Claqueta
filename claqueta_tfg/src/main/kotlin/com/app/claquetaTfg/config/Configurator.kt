package com.app.claquetaTfg.config


import com.app.jconfiglibrary.JConfig
import java.io.File

object Configurator {

    private lateinit var config: JConfig

    fun init(
	propertiesFilePath: String = "src/test/resources/configuration/configuracion.properties", 
	envFilePath: String = "src/test/resources/configuration/configuracion.env", 
	jsonFilePath: String = "src/test/resources/configuration/configuracion.json", 
	ymlFilePath: String = "src/test/resources/configuration/configuracion.yml", 
	hoconFilePath: String = "src/test/resources/configuration/configuracion.conf"
    ): Boolean {

        config = JConfig()
        var successfully = false

        if (config.parseFile(File(propertiesFilePath))){
            successfully = true
            println("Configuracion properties cargada correctamente")
        }else if (config.parseFile(File(envFilePath))){
            successfully = true
            println("Configuracion env cargada correctamente")
        }else if (config.parseFile(File(jsonFilePath))){
            successfully = true
            println("Configuracion json cargada correctamente")
        }else if (config.parseFile(File(ymlFilePath))){
            successfully = true
            println("Configuracion yml cargada correctamente")
        }else if (config.parseFile(File(hoconFilePath))){
            successfully = true
            println("Configuracion hocon cargada correctamente")
        }
        return successfully
    }

    fun getConfig(key: String): String? {
        return config.getString(key)
    }
}