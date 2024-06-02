package com.flppht.coffee_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flppht.coffee_server.model.Barista;
import com.flppht.coffee_server.service.BaristaService;

@RestController
@RequestMapping("/barista")
public class BaristaContoller {

    @Autowired
    private BaristaService baristaService;

    @GetMapping("/allBaristas")
    public ResponseEntity<List<Barista>> getAllBaristas() {
        return baristaService.getAllBaristas();
    }
}
