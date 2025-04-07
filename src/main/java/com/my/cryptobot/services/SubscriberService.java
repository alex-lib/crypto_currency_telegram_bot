//package com.my.cryptobot.services;
//
//import com.my.cryptobot.bot.command.StartCommand;
//import com.my.cryptobot.dto.SubscriberDto;
//import com.my.cryptobot.entities.Subscriber;
//import com.my.cryptobot.repositories.SubscriberRepository;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class SubscriberService {
//    private final SubscriberRepository repository;
//
//    public void create(Long id, String userName) {
//
//        if (StartCommand.getCommandIdentifier())
//
//        Subscriber subscriber = new Subscriber();
//        subscriber.setSubscriberId(id);
//        subscriber.setUserName(userName);
//        subscriber.setSubscriberUuid(UUID.randomUUID());
//        subscriber.setPriceToBuyBtc(null);
//        repository.save(subscriber);
//        log.info(userName + " is created as subscriber");
//    }
//
//    public static Subscriber mapToEntity(SubscriberDto dto) {
//        Subscriber subscriber = new Subscriber();
//        subscriber.setSubscriberId(dto.getSubscriberId());
//        subscriber.setUserName(dto.getUserName());
//        subscriber.setSubscriberUuid(dto.getSubscriberUuid());
//        subscriber.setPriceToBuyBtc(dto.getPriceToBuyBtc());
//        return subscriber;
//    }
//
//
//}
