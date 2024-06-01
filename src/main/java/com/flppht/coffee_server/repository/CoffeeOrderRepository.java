package com.flppht.coffee_server.repository;

import com.flppht.coffee_server.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Integer> {
    @Query(value = "SELECT * from coffee_order o where o.status=:status and o.is_coffee_to_go=true", nativeQuery = true)
    List<CoffeeOrder> findByStatusByCoffeeToGo(String status);

    @Query(value = "SELECT * from coffee_order o where o.status=:status and o.is_coffee_to_go=false", nativeQuery = true)
    List<CoffeeOrder> findByStatusByTable(String status);
}
