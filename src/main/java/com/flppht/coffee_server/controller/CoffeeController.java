package com.flppht.coffee_server.controller;

import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/allCoffees")
    public ResponseEntity<List<Coffee>> getAllCoffees () {
        return coffeeService.getAllCoffees();
    }

}
