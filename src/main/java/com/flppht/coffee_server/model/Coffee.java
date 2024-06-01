package com.flppht.coffee_server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
public class Coffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private double price;
    private String imageUrl;
    private int amountInGrams;
    private int timeToMakeInSec;

    public Coffee() {}

    public Coffee(String type, double price, String imageUrl, int amountInGrams, int timeToMakeInSec) {
        this.type = type;
        this.price = price;
        this.imageUrl = imageUrl;
        this.amountInGrams = amountInGrams;
        this.timeToMakeInSec = timeToMakeInSec;
    }
}
