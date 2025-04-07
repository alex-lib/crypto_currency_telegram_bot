package com.my.cryptobot.bot.command;

import com.my.cryptobot.client.BinanceClient;
import com.my.cryptobot.entities.Subscriber;
import com.my.cryptobot.exceptions.ParseDoubleException;
import com.my.cryptobot.repositories.SubscriberRepository;
import com.my.cryptobot.services.CryptoCurrencyService;
import com.my.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private SubscriberRepository subscriberRepository;
    private CryptoCurrencyService cryptoCurrencyService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();

        Subscriber subscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());
        if (subscriber.getPriceToBuyBtc() == null || subscriber.getPriceToBuyBtc() != null) {
           String[] args = message.toString().split(" ");
            double priceToBuy = Double.parseDouble(args[2]);
            subscriber.setPriceToBuyBtc(priceToBuy);

            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());

            try {
                String currentPriceBTC = TextUtil
                        .toString(cryptoCurrencyService.getBitcoinPrice());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            answer.setText(
                "Текущая цена BTC: " + cryptoCurrencyService + " USD\n" +
                "Новая подписка создана на стоимость BTC" + priceToBuy);

            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            //TODO: create an exception
//           try {
//               double priceToBuy = Double.parseDouble(args[2]);
//           } catch (ParseDoubleException e) {
//               throw new ParseDoubleException(e);
//           }
        }

    }
}