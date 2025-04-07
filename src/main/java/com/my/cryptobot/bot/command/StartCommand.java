package com.my.cryptobot.bot.command;


import com.my.cryptobot.entities.Subscriber;
import com.my.cryptobot.repositories.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.UUID;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    private SubscriberRepository subscriberRepository;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        User user = message.getFrom();

        findOrSaveSubscriber(user);

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                """);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }

    private Subscriber findOrSaveSubscriber(User user) {
        Subscriber persistantSubscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());
        if (persistantSubscriber == null) {
            Subscriber transientSubscriber = new Subscriber();
            transientSubscriber.setUserName(user.getUserName());
            transientSubscriber.setSubscriberId(user.getId());
            transientSubscriber.setPriceToBuyBtc(null);
            subscriberRepository.save(transientSubscriber);
        }
        return persistantSubscriber;
    }

}