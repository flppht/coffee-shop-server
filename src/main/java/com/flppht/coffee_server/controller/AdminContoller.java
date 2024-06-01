package com.flppht.coffee_server.controller;

import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminContoller {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping("/addCoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Coffee> addCoffee(@RequestBody Coffee coffee) {
        return coffeeService.addCoffee(coffee);
    }

    @PutMapping("/updateCoffee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCoffee (@RequestBody Coffee coffee) {
        return coffeeService.updateCoffee(coffee);
    }

    //only possible to delete coffee if it has not been used (due to referential integrity constraint violation)
    @DeleteMapping("/deleteCoffee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCoffee (@PathVariable int id) {
        return coffeeService.deleteCoffee(id);
    }
}
