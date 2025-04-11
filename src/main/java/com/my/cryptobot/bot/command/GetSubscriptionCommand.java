package com.my.cryptobot.bot.command;
import com.my.cryptobot.services.CryptoCurrencyService;
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
    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Returns the current subscription";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();
        Double currentSubscription = service.getCurrentSubscription(user);
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        try {
            if (currentSubscription == null) {
                answer.setText("You don't have subscription");
                absSender.execute(answer);
            } else {
                answer.setText("Current subscription: " + currentSubscription + " USD");
                absSender.execute(answer);
            }
        } catch (Exception e) {
            log.error("Error occurred in /get_subscription", e);
        }
    }
}