package ru.AlexeyFedechkin.telegramBot.ManBot

import com.google.common.base.Splitter
import mu.KotlinLogging
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.lang.reflect.Array.getChar
import kotlin.math.ceil
import kotlin.math.min


class TelegramBot : TelegramLongPollingBot {
    constructor(defaultBotOptions: DefaultBotOptions) : super(defaultBotOptions)
    constructor();

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
        }
    }

    /**
     * update handler
     * @param update
     */
    override fun onUpdateReceived(update: Update?) {
        if (update!!.hasMessage()){
            val chatId = update.message.chatId
            val result = net.find(update.message.text);
            if (result != null){
                send(result, chatId)
            } else {
                send("страница man не найдена", chatId)
            }
        }
    }

    /**
     * send file
     * @param file file with man page
     * @param chatId id of chat
     */
    private fun send(file: File, chatId: Long?) {
            val sendDocument = SendDocument();
            sendDocument.setDocument(file)
            sendDocument.chatId = chatId.toString()
            try {
                execute(sendDocument)
                logger.info("send file: " + file.name)
            }catch (e:TelegramApiException){
                logger.error("send message fail", e)
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
            logger.info("send message: " + text)
        }catch (e:TelegramApiException){
            logger.error("text send fail", e)
        }
    }

    /**
     * method that used by Telegram bot library to get username
     * @return username of bot
     */
    override fun getBotUsername(): String {
        return Config.username
    }

    /**
     *method that used by Telegram bot library to get bot token
     * @return bot token
     */
    override fun getBotToken(): String {
        return Config.token
    }

}