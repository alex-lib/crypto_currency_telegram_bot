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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;

@AllArgsConstructor
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Subscribes the user to the price of BTC";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        User user = message.getFrom();
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        Double priceToBuy = service.subscribeUser(user, message);

        if (priceToBuy != null) {
            try {
                String currentPriceBTC = TextUtil.toString(service.getBtcPrice());
                answer.setText("Текущая цена BTC: " + currentPriceBTC + " USD\n" +
                        "✅ Новая подписка создана на стоимость BTC " + priceToBuy);
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                log.error("Error occurred in /subscribe command", e);
            } catch (IOException e) {
                log.error("Failed to fetch BTC price", e);
            }
        } else {
            answer.setText("""
                    The target purchase price is specified incorrectly 
                    or is not specified at all 
                    (example of entering the value 85233.43)""");
            try {
                absSender.execute(answer);
            } catch (TelegramApiException e) {
                log.error("Error occurred in /subscribe command", e);
            }
        }
    }
}
