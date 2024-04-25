package com.app.claquetaTfg.logs

import com.app.claquetaTfg.config.Configurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger private constructor(){
    private lateinit var logger: Logger
    private lateinit var logInMemory: MutableList<Pair<String, String>>

    companion object {
        @Volatile private var instance: com.app.claquetaTfg.logs.Logger? = null

        fun instance(
            classType: Class<*> = this::class.java,
            configName: String
        ): com.app.claquetaTfg.logs.Logger {
            val configFile = Configurator.instance().getConfig(configName)
            System.setProperty("logback.configurationFile", configFile!!)
            instance ?: synchronized(this) {
                instance ?: Logger().also { instance = it }
            }
            instance!!.logger = LoggerFactory.getLogger(classType)
            instance!!.logInMemory = mutableListOf()

            return instance as com.app.claquetaTfg.logs.Logger
        }
    }

    fun trace(msg: String) {
        logger.trace(msg)
        this.logInMemory.add(Pair("trace",msg))
    }

    fun debug(msg: String) {
        logger.debug(msg)
        this.logInMemory.add(Pair("debug",msg))
    }

    fun info(msg: String) {
        logger.info(msg)
        this.logInMemory.add(Pair("info",msg))
    }

    fun warn(msg: String) {
        logger.warn(msg)
        this.logInMemory.add(Pair("warn",msg))
    }

    fun error(msg: String) {
        logger.error(msg)
        this.logInMemory.add(Pair("error",msg))
    }

    fun getLogsInMemory():MutableList<Pair<String, String>>{
        return this.logInMemory
    }
}