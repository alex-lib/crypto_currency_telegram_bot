package com.my.cryptobot.services;
import com.my.cryptobot.bot.CryptoBot;
import com.my.cryptobot.entities.Subscriber;
import com.my.cryptobot.repositories.SubscriberRepository;
import com.my.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class PriceNotificationService {
    private static final double PRICE_TOLERANCE = 150;
    private static final int CHECK_INTERVAL_MS = 120000;
    private static final long COOLDOWN_MINUTES = TimeUnit.MINUTES.toMillis(10);
    private final CryptoCurrencyService cryptoCurrencyService;
    private final SubscriberRepository subscriberRepository;
    private final CryptoBot cryptoBot;
    private final Map<Long, Instant> lastNotifications = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = CHECK_INTERVAL_MS)
    public void checkAndNotifySubscribers() {
        try {
            double currentBtcPrice = Double.parseDouble(TextUtil
                            .toString(cryptoCurrencyService.getBtcPrice()));
            notifyEligibleSubscribers(currentBtcPrice);
        } catch (IOException e) {
            log.error("Failed to fetch BTC price", e);
        }
    }

    private void notifyEligibleSubscribers(double currentPrice) {
        subscriberRepository.findAll().stream()
                .filter(subscriber -> shouldNotify(subscriber, currentPrice))
                .forEach(subscriber -> sendNotification(subscriber, currentPrice));
    }

    private boolean shouldNotify(Subscriber subscriber, double currentPrice) {
        if (subscriber.getPriceToBuyBtc() == null) return false;
        Instant lastNotification = lastNotifications.get(subscriber.getSubscriberId());

        if (lastNotification != null &&
                lastNotification.plus(Duration.ofMinutes(COOLDOWN_MINUTES)).isAfter(Instant.now())) {
            return false;
        }
        return Math.abs(currentPrice - subscriber.getPriceToBuyBtc()) <= PRICE_TOLERANCE;
    }

    private void sendNotification(Subscriber subscriber, double currentPrice) {
        try {
            cryptoBot.execute(SendMessage.builder()
                    .chatId(subscriber.getSubscriberId().toString())
                    .text(createNotification(currentPrice, subscriber.getPriceToBuyBtc()))
                    .build());

            lastNotifications.put(subscriber.getSubscriberId(), Instant.now());
            log.info("Sent notification to {}", subscriber.getSubscriberId());
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to {}", subscriber.getSubscriberId(), e);
        }
    }

    private String createNotification(double currentPrice, double targetPrice) {
        return String.format("""
            🚨 BTC Price Notification!
            Current: $%.2f
            Your Target: $%.2f""",
                currentPrice,
                targetPrice);
    }
}