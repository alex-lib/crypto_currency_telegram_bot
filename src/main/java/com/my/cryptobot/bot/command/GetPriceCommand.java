package com.my.cryptobot.bot.command;
import com.my.cryptobot.services.CryptoCurrencyService;
import com.my.cryptobot.utils.TextUtil;
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
public class GetPriceCommand implements IBotCommand {
    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "get_price";
    }

    @Override
    public String getDescription() {
        return "Returns the price of BTC in USD";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            answer.setText("Current price of BTC: " + TextUtil.toString(service.getBtcPrice()) + " USD");
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Error occurred in /get_price", e);
        }
    }
}