package ru.AlexeyFedechkin.telegramBot.ManBot

import mu.KotlinLogging
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import kotlin.system.exitProcess


class TelegramBot : TelegramLongPollingBot {
    constructor(defaultBotOptions: DefaultBotOptions) : super(defaultBotOptions)
    constructor()

    private val logger = KotlinLogging.logger {}
    private val net = Net()

    /**
     *init telegram bot
     */
    fun init(){
        try {
            if (Config.isUserProxy){
                val botsApi = TelegramBotsApi()
                val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
                botOptions.proxyHost = Config.proxyHost
                botOptions.proxyPort = Config.proxyPort
                botOptions.proxyType = DefaultBotOptions.ProxyType.SOCKS5
                logger.info("proxy configure")
                botsApi.registerBot(TelegramBot(botOptions))
            } else {
                val botsApi = TelegramBotsApi()
                botsApi.registerBot(TelegramBot())
            }
            logger.info("bot register")
        } catch (e:TelegramApiException){
            logger.error("bot start fail", e)
            exitProcess(1)
        }
    }

    /**
     * update handler
     * @param update
     */
    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasMessage()){
            val chatId = update.message.chatId
            val message = update.message.text
            if (message == "/start"){
                send("Здраствуйте, это бот для получения man страниц. Просто, введите имя программы и бот" +
                        " пришлет вам текстоый файл с man страницей для нее", chatId)
                return
            } else if (message.startsWith("/")){
                return
            }
            val result = net.find(update.message.text)
            if (result != null){
                send(result, chatId)
            } else {
                send("страница man не найдена", chatId)
            }
        }
    }

    /**
     * send file
     * if file id is cached sendingg by file id
     * @param file file with man page
     * @param chatId id of chat
     */
    private fun send(file: File, chatId: Long) {
        val sendDocument = SendDocument()
        sendDocument.chatId = chatId.toString()
        try {
            if (Cache.contain(file.name)){
                sendDocument.setDocument(Cache.get(file.name))
                execute(sendDocument)
                logger.info("send file by fileId")
            } else {
                sendDocument.setDocument(file)
                val res = execute(sendDocument)
                Cache.add(file.name, res.document.fileId)
                logger.info("send file: " + file.name)
            }
        }catch (e:TelegramApiException){
            logger.info("send file fail", e)
        }
    }

    /**
     * send text
     * @param text text
     * @param chatId id of chat
     */
    private fun send(text:String, chatId: Long){
        val sendMessage = SendMessage()
        sendMessage.text = text
        sendMessage.chatId = chatId.toString()
        try {
            execute(sendMessage)
            logger.info("send message: $text")
        }catch (e:TelegramApiException){
            logger.error("text send fail", e)
        }
    }

    /**
     * method that used by Telegram bot library to get username
     * @return username of bot
     */
    override fun getBotUsername(): String {
        return if (Config.isTesting){
            Config.testingUsername
        } else{
            Config.username
        }
    }

    /**
     *method that used by Telegram bot library to get bot token
     * @return bot token
     */
    override fun getBotToken(): String {
        return if (Config.isTesting){
            Config.testingToken
        } else{
            Config.token
        }
    }
}