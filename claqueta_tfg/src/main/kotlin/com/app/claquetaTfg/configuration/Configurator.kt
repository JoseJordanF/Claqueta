package com.app.claquetaTfg.configuration

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.github.cdimascio.dotenv.Dotenv
import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Configurator {

    private lateinit var config: Config

    fun init(){
        config = ConfigFactory.empty()
    }

    fun loadConfig(pathFile: String) {
        val separador = if (pathFile.contains('/')) '/' else '\\'

        when {
            pathFile.endsWith(".env") -> {
                var directorio = pathFile.substringBeforeLast(separador)
                var archivo = pathFile.substringAfterLast(separador)
                val dotenv = Dotenv
                    .configure()
                    .directory(directorio)
                    .filename(archivo)
                    .load()
                val envMap = dotenv.entries().associateBy({ it.key }, { it.value })
                val properties = Properties()
                properties.putAll(envMap)
                config = config.withFallback(ConfigFactory.parseProperties(properties))
            }
            pathFile.endsWith(".json") -> {
                val jsonConfig: Config = ConfigFactory.parseFile(File(pathFile))
                config = config.withFallback(jsonConfig)
            }
            pathFile.endsWith(".properties") -> {
                val properties = Properties()
                FileInputStream(pathFile).use { input ->
                    properties.load(input)
                }
                val propertiesConfig: Config = ConfigFactory.parseProperties(properties)
                config = config.withFallback(propertiesConfig)
            }
            pathFile.endsWith(".conf") -> {
                val hoconConfig: Config = ConfigFactory.parseFile(File(pathFile))
                config = config.withFallback(hoconConfig)
            }
            else -> {
                try {
                    val textStringConfig: Config = ConfigFactory.parseString(pathFile)
                    config = config.withFallback(textStringConfig)
                }catch (e: Exception){
                    println("Error: extensión de archivo no reconocida: ${e.message}")
                }
            }
        }
    }

    fun getConfig(key: String) = config.getString(key)
}