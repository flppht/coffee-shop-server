package com.flppht.coffee_server.service;

import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public ResponseEntity<List<Coffee>> getAllCoffees() {
        List<Coffee> coffeeList = coffeeRepository.findAll();
        if(!coffeeList.isEmpty()) {
            return new ResponseEntity<>(coffeeList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Coffee> addCoffee(@RequestBody Coffee coffee) {
        coffeeRepository.save(coffee);
        return new ResponseEntity<>(coffee, HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateCoffee(Coffee coffee) {
        coffeeRepository.save(coffee);
        return new ResponseEntity<>("Coffee successfully updated.", HttpStatus.OK);
    }

    //only possible to delete coffee if it has not been used (due to referential integrity constraint violation)
    public ResponseEntity<String> deleteCoffee(int id) {
        coffeeRepository.deleteById(id);
        return new ResponseEntity<>("Coffee deleted successfully.", HttpStatus.OK);
    }
}
