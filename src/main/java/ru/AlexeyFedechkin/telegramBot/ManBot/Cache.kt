package ru.AlexeyFedechkin.telegramBot.ManBot

object Cache {
    private val data = HashMap<String, String>()
    /**
     * check if there is a given program in the cache
     * @param name name of program
     * @return true if program contained in the cache
     */
    fun contain(name:String):Boolean{
        return data.containsKey(name)
    }

    /**
     * add file id to cache
     * @param name name of man page
     * @param fileId file id of man page
     */
    fun add(name:String, fileId:String){
        data[name] = fileId
    }

    /**
     * get file id
     * @param name name of program
     * @return file id of man page
     */
    fun get(name:String):String{
        return data[name].toString()
    }
}