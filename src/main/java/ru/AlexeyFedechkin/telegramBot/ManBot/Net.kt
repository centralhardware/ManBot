package ru.AlexeyFedechkin.telegramBot.ManBot

import mu.KotlinLogging
import org.jsoup.Jsoup
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class Net {
    private val logger = KotlinLogging.logger {  }
    private val BASE_URL = "http://man.he.net/man"
    private val BASE_PATH = "man/"
    /**
     *find man page
     * @param name name of unix program
     * @return file with man page from url or file
     */
    fun find(name:String): File?{
        val path = BASE_PATH + name;
        if (File(path).isFile){
            return File(path)
        } else{
            val res = getStringFromUrl(name)
            if (res != null){
                return stringToFile(res, name)
            }
        }
        return null
    }

    /**
     *get string from site
     * @param name name of unix program
     * @return string with man page from site
     */
    private fun getStringFromUrl(name:String):String?{
        for (i in 0..8){
            val url = URL(BASE_URL+ i + "/" +  name)
            val urlConnection =url.openConnection() as HttpURLConnection
            try {
                val text = urlConnection.inputStream.bufferedReader().readText()
                logger.info("get data from: " + url)
                if (validate(text, name)){
                    return clean(text)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
        return null;
    }

    private val copyright = "Man Pages Copyright Respective Owners. Site Copyright (C) 1994 - 2019 Hurricane Electric. All Rights Reserved."
    /**
     * delete all html teg
     * @param text html text
     * @return string without html text
     */
    private fun clean(text: String): String {
        return Jsoup.parse(text).text().replace(copyright, "")
    }

    /**
     * check that text contain man page with giving name
     * @param text html page
     * @param name name o unix program
     * @return true if text contain man page
     */
    private fun validate(text: String,name:String ): Boolean {
        return !text.contains("No matches for")
    }

    /**
     * save String to file
     * @param text content of file
     * @param name name of man page for path to file
     * @return saved file
     */
    private fun stringToFile(text:String, name:String): File{
        val path = BASE_PATH + name;
        val file = File(path)
        file.createNewFile()
        file.writeText(text)
        logger.info("create file: " + path)
        return file
    }
}