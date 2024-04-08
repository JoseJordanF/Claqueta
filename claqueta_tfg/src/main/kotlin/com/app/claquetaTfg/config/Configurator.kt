package com.app.claquetaTfg.config

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
        val fileConfig: Config = ConfigFactory.parseFile(File(pathFile))
        config = config.withFallback(fileConfig)
    }

    fun getConfig(key: String) = config.getString(key)
}