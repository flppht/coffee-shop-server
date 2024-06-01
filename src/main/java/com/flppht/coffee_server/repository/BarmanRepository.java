package com.flppht.coffee_server.repository;

import com.flppht.coffee_server.model.Barman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarmanRepository extends JpaRepository<Barman, Integer> {
}
