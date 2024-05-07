package com.app.claquetaTfg.logs

import ch.qos.logback.classic.Level
import ch.qos.logback.core.encoder.Encoder
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Context
import com.app.claquetaTfg.util.Constants.loggerLevel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import net.logstash.logback.decorate.CompositeJsonGeneratorDecorator
import net.logstash.logback.decorate.PrettyPrintingJsonGeneratorDecorator
import net.logstash.logback.encoder.LogstashEncoder
import org.slf4j.LoggerFactory
import org.slf4j.spi.LoggingEventBuilder

class Logger private constructor() : LoggerInterface, LogbackInterface {
    private lateinit var logger: Logger
    private lateinit var memoryAppender: MemoryAppender

    companion object {
        @Volatile
        private var instance: SimpleLogger? = null

        fun instance(
        ): SimpleLogger {

            return instance ?: synchronized(this) {
                instance ?: SimpleLogger().also {
                    it.initLogger()
                    instance = it
                }
            }
        }
    }

    private fun initLogger() {
        logger = LoggerFactory.getLogger(this::class.java) as Logger
        logger.level = loggerLevel
        logger.isAdditive = false
        val context = logger.loggerContext
        val encoder = createEncoder(context)
        memoryAppender = createAppender()
        memoryAppender.context = context
        memoryAppender.encoder = encoder
        memoryAppender.start()
        logger.addAppender(memoryAppender)
    }

    override fun log(msg: Any, argArray: Array<out Any?>) {
        val context = logBase()
        var cont = 0
        for (arg in argArray) {
            cont++
            context!!.addKeyValue("item_involved_$cont", arg)
        }
        context
            ?.setMessage(msg.toString())
            ?.log()
    }

    override fun error(msg: Any, argArray: Array<out Any?>) {
        val context = logger.atError()
        var cont = 0
        for (arg in argArray) {
            cont++
            context!!.addKeyValue("item_involved_$cont", arg)
        }
        context
            ?.setMessage(msg.toString())
            ?.log()
    }

    override fun historyLogs(): List<HashMap<String, Any>> {
        val logsInMemory: MutableList<HashMap<String, Any>> = mutableListOf()
        memoryAppender.logInMemory.forEach {
            val jsonObject = Json.parseToJsonElement(it).jsonObject
            val mapLogs: HashMap<String, Any> = hashMapOf()
            jsonObject.forEach { (key, jsonElement) ->
                mapLogs[key] = when {
                    jsonElement is JsonPrimitive -> jsonElement.content
                    jsonElement is JsonArray -> jsonElement.joinToString(",")
                    else -> jsonElement.toString()
                }
            }
            logsInMemory.add(mapLogs)
        }
        return logsInMemory
    }

    override fun createAppender(): MemoryAppender {
        return MemoryAppender()
    }

    override fun createEncoder(context: Any): Encoder<ILoggingEvent> {
        val encoder = LogstashEncoder()
        encoder.context = context as Context
        val prettyPrint = PrettyPrintingJsonGeneratorDecorator()
        val composite = CompositeJsonGeneratorDecorator()
        composite.addDecorator(prettyPrint)
        encoder.jsonGeneratorDecorator = composite
        encoder.start()
        return encoder
    }

    private fun logBase(): LoggingEventBuilder? {
        return when (logger.level) {
            Level.TRACE -> logger.atTrace()
            Level.DEBUG -> logger.atDebug()
            Level.INFO -> logger.atInfo()
            else -> logger.atInfo()
        }
    }

}

class LoggerManager(private val logger: LoggerInterface) {
    fun log(msg: Any, argArray: Array<out Any?>) {
        logger.log(msg, argArray)
    }

    fun error(msg: Any, argArray: Array<out Any?>) {
        logger.error(msg, argArray)
    }

    fun historyLogs(): Any {
        return logger.historyLogs()
    }
}