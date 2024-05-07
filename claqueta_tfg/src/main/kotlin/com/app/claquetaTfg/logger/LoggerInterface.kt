package com.app.claquetaTfg.logs

interface LoggerInterface{
    fun log(msg:Any, argArray: Array<out Any?>)
    fun error(msg:Any, argArray: Array<out Any?>)
    fun historyLogs():Any
}