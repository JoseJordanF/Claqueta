package com.app.claquetaTfg.logs

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.encoder.Encoder

class MemoryAppender : AppenderBase<ILoggingEvent>(){

    var encoder: Encoder<ILoggingEvent>? = null
    val logInMemory: MutableList<String> = mutableListOf()

    override fun append(eventObject: ILoggingEvent) {
        encoder?.let {
            val encodedBytes = it.encode(eventObject)
            logInMemory.add(String(encodedBytes))
        }
    }
}