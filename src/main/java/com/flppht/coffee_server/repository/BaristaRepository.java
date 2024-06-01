package com.flppht.coffee_server.repository;

import com.flppht.coffee_server.model.Barista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaristaRepository extends JpaRepository<Barista, Integer> {
}
