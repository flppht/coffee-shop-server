package com.flppht.coffee_server.controller;


import com.flppht.coffee_server.model.CoffeeOrder;
import com.flppht.coffee_server.model.CoffeeReqBody;
import com.flppht.coffee_server.service.BarmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barman")
public class BarmanController {
    @Autowired
    private BarmanService barmanService;

    @PostMapping("coffee-to-go/{coffeeId}")
    public ResponseEntity<String> createOrderCoffeeToGo (@PathVariable int coffeeId) {
        return barmanService.createOrderCoffeeToGo(coffeeId);
    }

    @PostMapping("table")
    public ResponseEntity<List<CoffeeOrder>> createOrderAtTable (@RequestBody List<CoffeeReqBody> coffeeList) {
        return barmanService.createOrderAtTable(coffeeList);
    }

    @GetMapping("status/{orderId}")
    public ResponseEntity<String> statusOfOrder(@PathVariable int orderId) {
        return barmanService.statusOfOrder(orderId);
    }
}
