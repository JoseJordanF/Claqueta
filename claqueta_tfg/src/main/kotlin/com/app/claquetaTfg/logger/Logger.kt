package com.app.claquetaTfg.logs

import com.app.claquetaTfg.config.Configurator
import com.app.claquetaTfg.util.Constants.propSystemLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger private constructor() {
    private lateinit var logger: Logger

    companion object {
        @Volatile
        private var instance: com.app.claquetaTfg.logs.Logger? = null

        fun instance(
            classType: Class<*> = this::class.java,
            configName: String,
        ): com.app.claquetaTfg.logs.Logger {
            val configFile = Configurator.instance().getConfig(configName)
            System.setProperty(propSystemLog, configFile!!)
            instance ?: synchronized(this) {
                instance ?: Logger().also { instance = it }
            }
            instance!!.logger = LoggerFactory.getLogger(classType)

            return instance as com.app.claquetaTfg.logs.Logger
        }
    }

    fun trace(msg: String) {
        logger.trace(msg)
        ManagerLogger.addLog(Pair("trace", msg))
    }

    fun debug(msg: String) {
        logger.debug(msg)
        ManagerLogger.addLog(Pair("debug", msg))
    }

    fun info(msg: String) {
        logger.info(msg)
        ManagerLogger.addLog(Pair("info", msg))
    }

    fun warn(msg: String) {
        logger.warn(msg)
        ManagerLogger.addLog(Pair("warn", msg))
    }

    fun error(msg: String) {
        logger.error(msg)
        ManagerLogger.addLog(Pair("error", msg))
    }
    
}