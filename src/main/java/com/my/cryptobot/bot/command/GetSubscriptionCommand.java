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

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private SubscriberRepository subscriberRepository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();
        Subscriber subscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());


        try {
            if (subscriber.getPriceToBuyBtc() == null) {
                SendMessage answer = new SendMessage();
                answer.setChatId(message.getChatId());
                answer.setText("У вас нет текущей подписки");
                absSender.execute(answer);
            } else {
                SendMessage answer = new SendMessage();
                answer.setChatId(message.getChatId());
                answer.setText("Текущая подписка: " + subscriber.getPriceToBuyBtc() + " USD");
                absSender.execute(answer);
            }
        }catch (Exception e) {
            e.printStackTrace();
    }


    }
}