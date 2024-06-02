package com.flppht.coffee_server.service.impl;

import com.flppht.coffee_server.constants.StatusConstants;
import com.flppht.coffee_server.model.Barista;
import com.flppht.coffee_server.model.Coffee;
import com.flppht.coffee_server.model.CoffeeOrder;
import com.flppht.coffee_server.repository.BaristaRepository;
import com.flppht.coffee_server.repository.CoffeeOrderRepository;
import com.flppht.coffee_server.repository.CoffeeRepository;
import com.flppht.coffee_server.service.BaristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class BaristaServiceImpl implements BaristaService {
    @Autowired
    private BaristaRepository baristaRepository;

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    @Autowired
    private CoffeeRepository coffeeRepository;

    private final static int MAX_ORDERS_TO_ACCEPT = 5;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void assignOrderToBarista(CoffeeOrder coffeeOrder) {
        List<Barista> baristas = baristaRepository.findAll();
        for (Barista barista : baristas) {
            if (!barista.isBusy() && !barista.isRefilling() && barista.getCoffeeAmount() > 0) {
                barista.setBusy(true);
                barista.setCoffeeAmount(barista.getCoffeeAmount() - coffeeOrder.getCoffee().getAmountInGrams());
                baristaRepository.save(barista);

                coffeeOrder.setStatus(StatusConstants.IN_PROGRESS);
                coffeeOrder.setBarista(barista);
                coffeeOrderRepository.save(coffeeOrder);

                // time reserved for making coffee
                scheduler.schedule(() -> {
                    barista.setBusy(false);
                    baristaRepository.save(barista);
                    coffeeOrder.setStatus(StatusConstants.COMPLETED);
                    coffeeOrderRepository.save(coffeeOrder);
                    // after coffee is made, check if there's enough coffee to proceed
                    // if not, refill the grinder and be unavailable for 2 min
                    if (barista.getCoffeeAmount() < minCoffeeAmountInGrinder()) {
                        refillCoffee(barista);
                    }
                    processNextOrder();

                }, coffeeOrder.getCoffee().getTimeToMakeInSec(), TimeUnit.SECONDS);

                return;
            }
        }

        // if coffee to go and in case there's no available barista, try to add order to
        // order queue
        if (coffeeOrder.isCoffeeToGo()) {
            addToPendingQueue(coffeeOrder);
        }
    }

    public ResponseEntity<List<Barista>> getAllBaristas() {
        return new ResponseEntity<>(baristaRepository.findAll(), HttpStatus.OK);
    }

    private void refillCoffee(Barista barista) {
        barista.setRefilling(true);
        baristaRepository.save(barista);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                barista.setCoffeeAmount(300);
                barista.setRefilling(false);
                baristaRepository.save(barista);
                processNextOrder();
            }
        }, 120000);
    }

    private void processNextOrder() {
        // first proceed with coffee to go orders if exists
        List<CoffeeOrder> coffeeToGoPendingOrders = coffeeOrderRepository
                .findByStatusByCoffeeToGo(StatusConstants.PENDING);
        if (!coffeeToGoPendingOrders.isEmpty()) {
            CoffeeOrder nextOrder = coffeeToGoPendingOrders.get(0);
            assignOrderToBarista(nextOrder);
        } else {
            // if not, proceed with table orders
            List<CoffeeOrder> tablePendingOrders = coffeeOrderRepository.findByStatusByTable(StatusConstants.PENDING);
            if (!tablePendingOrders.isEmpty()) {
                CoffeeOrder nextOrder = tablePendingOrders.get(0);
                assignOrderToBarista(nextOrder);
            }
        }
    }

    private void addToPendingQueue(CoffeeOrder coffeeOrder) {
        int pendingOrdersCount = pendingOrdersCount();
        if (pendingOrdersCount < MAX_ORDERS_TO_ACCEPT) {
            coffeeOrderRepository.save(coffeeOrder);
        } else {
            throw new RuntimeException(
                    "All baristas are busy and the pending order list is full. Your order cannot be accepted right now.");
        }
    }

    private Integer pendingOrdersCount() {
        return coffeeOrderRepository.findByStatusByCoffeeToGo(StatusConstants.PENDING).size();
    }

    // minimum amount of coffee in grinder so at least one coffee can be made
    private int minCoffeeAmountInGrinder() {
        List<Coffee> coffeeAmount = coffeeRepository.findAll();
        // find coffee with max value of grams
        Coffee coffee = coffeeAmount.stream().max(Comparator.comparing(Coffee::getAmountInGrams))
                .orElseThrow(NoSuchElementException::new);
        return coffee.getAmountInGrams();
    }
}
