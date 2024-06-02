package com.flppht.coffee_server.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.flppht.coffee_server.model.Barista;
import com.flppht.coffee_server.model.CoffeeOrder;

public interface BaristaService {

    void assignOrderToBarista(CoffeeOrder coffeeOrder);

    ResponseEntity<List<Barista>> getAllBaristas();
}
