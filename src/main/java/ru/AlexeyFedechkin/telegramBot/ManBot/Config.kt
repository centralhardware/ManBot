package ru.AlexeyFedechkin.telegramBot.ManBot

import java.util.*

object Config {
        private val config = ResourceBundle.getBundle("config")
        val token : String = config.getString("BOT_TOKEN")
        val username : String = config.getString("BOT_USERNAME")
        val proxyHost : String = config.getString("PROXY_HOST")
        val proxyPort : Int = config.getString("PROXY_PORT").toInt()
        val isUserProxy : Boolean = config.getString("IS_USE_PROXY").toBoolean()
}