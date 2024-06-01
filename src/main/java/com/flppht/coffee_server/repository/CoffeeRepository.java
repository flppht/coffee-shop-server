package com.flppht.coffee_server.repository;

import com.flppht.coffee_server.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Integer> {
}
