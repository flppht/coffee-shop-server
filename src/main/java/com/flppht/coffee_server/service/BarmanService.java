package com.flppht.coffee_server.service;

import com.flppht.coffee_server.constants.StatusConstants;
import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.model.CoffeeOrder;
import com.flppht.coffee_server.model.CoffeeReqBody;
import com.flppht.coffee_server.repository.CoffeeRepository;
import com.flppht.coffee_server.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BarmanService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private BaristaService baristaService;

    public ResponseEntity<String> createOrderCoffeeToGo(int coffeeId) {
        Optional<Coffee> coffee = coffeeRepository.findById(coffeeId);

        CoffeeOrder coffeeOrder = new CoffeeOrder();
        try {
            coffeeOrder.setCoffee(coffee.get());
        } catch (Exception e) {
            return new ResponseEntity<>("Coffee not found!", HttpStatus.NOT_FOUND);
        }
        coffeeOrder.setStatus(StatusConstants.PENDING);
        coffeeOrder.setCoffeeToGo(true);

        try {
            baristaService.assignOrderToBarista(coffeeOrder);
            if (Objects.equals(coffeeOrder.getStatus(), StatusConstants.PENDING)) {
                return new ResponseEntity<>(
                        "Your order is added to pending list, please wait. Order no." + coffeeOrder.getId(),
                        HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Your order is accepted. Order no." + coffeeOrder.getId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<CoffeeOrder>> createOrderAtTable(List<CoffeeReqBody> coffeeList) {
        List<CoffeeOrder> order = new ArrayList<>();
        for (CoffeeReqBody c : coffeeList) {
            for (int i = 0; i < c.getCount(); i++) {
                Optional<Coffee> coffee = coffeeRepository.findById(c.getId());

                CoffeeOrder coffeeOrder = new CoffeeOrder();
                coffeeOrder.setCoffee(coffee.get());
                coffeeOrder.setStatus(StatusConstants.PENDING);
                coffeeOrder.setCoffeeToGo(false);
                coffeeOrderRepository.save(coffeeOrder);
                order.add(coffeeOrder);

                baristaService.assignOrderToBarista(coffeeOrder);
            }
        }

        return new ResponseEntity<>(order,
                HttpStatus.CREATED);
    }

    public ResponseEntity<String> statusOfOrder(int orderId) {
        Optional<CoffeeOrder> coffeeOrder = coffeeOrderRepository.findById(orderId);
        return coffeeOrder.map(order -> new ResponseEntity<>(order.getStatus(),
                HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Order does not exist!",
                        HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<CoffeeOrder>> getPendingOrders() {
        return new ResponseEntity<>(coffeeOrderRepository.findByStatusByCoffeeToGo(StatusConstants.PENDING),
                HttpStatus.OK);
    }

    public ResponseEntity<List<CoffeeOrder>> getCompletedOrders() {
        return new ResponseEntity<>(coffeeOrderRepository.findByStatus(StatusConstants.COMPLETED),
                HttpStatus.OK);
    }
}
