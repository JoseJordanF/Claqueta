package com.app.claquetaTfg.logs

import com.app.claquetaTfg.config.Configurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger {
     lateinit var logger: Logger


    companion object {
        private lateinit var instance: com.app.claquetaTfg.logs.Logger

        fun instance(clazz: Class<*> = this::class.java): com.app.claquetaTfg.logs.Logger {
            val configFile = Configurator.instance().getConfig("CLAQUETA_LOGBACK_CONFIG")
            System.setProperty("logback.configurationFile", configFile!!)
            instance = Logger()
            instance.logger = LoggerFactory.getLogger(clazz)

            return instance
        }
    }

    fun trace(msg: String) {
        logger.trace(msg)
    }

    fun debug(msg: String) {
        logger.debug(msg)
    }

    fun info(msg: String) {
        logger.info(msg)
    }

    fun warn(msg: String) {
        logger.warn(msg)
    }

    fun error(msg: String) {
        logger.error(msg)
    }
}