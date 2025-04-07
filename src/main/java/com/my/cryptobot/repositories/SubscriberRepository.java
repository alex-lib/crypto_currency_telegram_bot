package com.my.cryptobot.repositories;

import com.my.cryptobot.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository  extends JpaRepository<Subscriber, Integer> {
    Subscriber findSubscriberBySubscriberId(Long subscriberId);
//    @Query("SELECT s FROM Subscriber s WHERE s.subscriberUuid = :subscriberUuid")
//    Optional<Subscriber> findByFindByUUID(String subscriberUUID);
}
