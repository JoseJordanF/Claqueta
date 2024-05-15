package com.app.claquetaTfg.logs

interface LogbackInterface {
    fun createAppender():Any
    fun createEncoder(context: Any):Any
}