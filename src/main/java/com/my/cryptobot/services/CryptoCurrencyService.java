package com.my.cryptobot.services;
import com.my.cryptobot.client.BinanceClient;
import com.my.cryptobot.entities.Subscriber;
import com.my.cryptobot.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
@Slf4j
public class CryptoCurrencyService {
    private final SubscriberRepository subscriberRepository;
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;

    public double getBtcPrice() throws IOException {
        price.set(client.getBitcoinPrice());
        log.info("Current price of BTC is provided");
        return price.get();
    }

    public void findOrSaveSubscriber(User user) {
        Subscriber persistantSubscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());

        if (persistantSubscriber != null) {
            persistantSubscriber.setUserName(user.getUserName());
            log.info("User is found and updated - {}", user.getId());
        }

        if (persistantSubscriber == null) {
            Subscriber transientSubscriber = new Subscriber();
            transientSubscriber.setUserName(user.getUserName());
            transientSubscriber.setSubscriberId(user.getId());
            transientSubscriber.setPriceToBuyBtc(null);
            subscriberRepository.save(transientSubscriber);
            log.info("New user is saved - {}", user.getId());
        }
    }

    public Double getCurrentSubscription(User user) {
        Subscriber subscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());
        log.info("Current subscription is provided - {}", user.getId());
        return subscriber.getPriceToBuyBtc();
    }

    public Double subscribeUser(User user, Message message) {
        Subscriber subscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());
        String[] args = message.getText().split(" ");

        if (checkValue(args)) {
            Double priceToBuy = Double.parseDouble(args[1]);
            subscriber.setPriceToBuyBtc(priceToBuy);
            subscriberRepository.save(subscriber);
            log.info("Subscription is saved - {}", user.getId());
            return priceToBuy;
        }
        log.info("Subscription isn't saved - {}", user.getId());
        return null;
    }

    private boolean checkValue(String[] args) {
        String regex = "^[0-9]+(\\.[0-9]{1,3})?$";

        if (args.length != 2 || !args[1].matches(regex)) {
            log.error("Invalid price format");
            return false;
        }
        return true;
    }

    public void deleteSubscriber(User user) {
        Subscriber subscriber = subscriberRepository.findSubscriberBySubscriberId(user.getId());
        subscriber.setPriceToBuyBtc(null);
        subscriberRepository.save(subscriber);
        log.info("Subscription is deleted - {}", user.getId());
    }
}
