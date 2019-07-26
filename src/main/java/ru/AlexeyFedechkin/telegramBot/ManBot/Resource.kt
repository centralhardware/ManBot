package ru.AlexeyFedechkin.telegramBot.ManBot

import java.util.*
class Resource {
    private val resource = ResourceBundle.getBundle("string")

    fun get(key:String): String{
        return resource.getString(key);
    }
}
