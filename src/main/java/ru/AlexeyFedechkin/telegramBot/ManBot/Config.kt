package ru.AlexeyFedechkin.telegramBot.ManBot

import java.util.*

object Config {
        private val config = ResourceBundle.getBundle("config")
        val token : String = config.getString("BOT_TOKEN")
        val testingToken : String = config.getString("BOT_TESTING_USERNAME")
        val username : String = config.getString("BOT_USERNAME")
        val testingUsername : String = config.getString("BOT_TESTING_TOKEN")
        val proxyHost : String = config.getString("PROXY_HOST")
        val proxyPort : Int = config.getString("PROXY_PORT").toInt()
        val isUserProxy : Boolean = config.getString("IS_USE_PROXY").toBoolean()
        val isTesting : Boolean = config.getString("IS_TESTING").toBoolean()
}