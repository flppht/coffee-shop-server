package com.flppht.coffee_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Barman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
