package com.app.claquetaTfg.deployment


import com.app.claquetaTfg.deployment.plugins.configureRouting
import com.app.claquetaTfg.util.Constants.ktorHost
import com.app.claquetaTfg.util.Constants.ktorPort
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = ktorPort, host = ktorHost, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
}