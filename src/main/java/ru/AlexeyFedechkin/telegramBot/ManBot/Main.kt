package ru.AlexeyFedechkin.telegramBot.ManBot

import mu.KotlinLogging
import org.telegram.telegrambots.ApiContextInitializer

private val logger = KotlinLogging.logger {}
/**
 * TODO
 *
 * @param args never use
 */
fun main(args: Array<String>){
    logger.info("program start")
    ApiContextInitializer.init();
    logger.info("api context init")
    TelegramBot().init()
    logger.info("telegram bot start")
}