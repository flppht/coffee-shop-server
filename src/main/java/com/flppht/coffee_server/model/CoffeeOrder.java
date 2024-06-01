package com.flppht.coffee_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CoffeeOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Coffee coffee;
    @ManyToOne
    private Barista barista;
    private String status; // "PENDING", "IN_PROGRESS", "COMPLETED"
    private boolean isCoffeeToGo;
}
