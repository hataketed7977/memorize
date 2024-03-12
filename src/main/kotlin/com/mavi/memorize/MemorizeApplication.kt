package com.mavi.memorize

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import java.awt.Desktop
import java.net.URI


@SpringBootApplication
class MemorizeApplication

fun main(args: Array<String>) {
    val app = runApplication<MemorizeApplication>(*args)
    openHomePage(app)

}

fun openHomePage(app: ConfigurableApplicationContext) {
    val open = app.environment.getProperty("browser.open").toBoolean()
    if (!open) return
    try {
        val host = app.environment.getProperty("browser.host").toString()
        val port = app.environment.getProperty("browser.port").toString()
        val path = app.environment.getProperty("browser.path").toString()
        System.setProperty("java.awt.headless", "false")
        Desktop.getDesktop().browse(URI("$host:$port$path"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
