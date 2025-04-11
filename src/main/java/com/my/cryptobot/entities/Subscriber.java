package com.my.cryptobot.entities;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @Column(name = "id")
    private Long subscriberId;
    @Column(name = "user_name")
    private String userName;
    @Nullable
    @Column(name = "price_to_buy_btc")
    private Double priceToBuyBtc;
}
