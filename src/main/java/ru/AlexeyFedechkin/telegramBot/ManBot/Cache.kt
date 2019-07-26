package ru.AlexeyFedechkin.telegramBot.ManBot

object Cache {
    val data = HashMap<String, String>()
    fun contain(name:String):Boolean{
        return data.containsKey(name)
    }

    fun add(name:String, fileId:String){
        data.put(name, fileId)
    }

    fun get(name:String):String{
        return data.get(name).toString()
    }
}