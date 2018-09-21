package com.iceberg.coffeemachine.controller;

import com.iceberg.coffeemachine.model.CoffeeMachine;
import com.iceberg.coffeemachine.service.CoffeeMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/do")
public class CoffeeMachineController {


    private CoffeeMachineService coffeeMachineService;

    @Autowired
    public CoffeeMachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    @GetMapping
    public ResponseEntity getCoffee(@RequestParam("coffeeType") String coffeeType) {
        return coffeeMachineService.doCoffee(coffeeType.toUpperCase());
    }

    @GetMapping(value = "/update")
    public ResponseEntity updateResources(@RequestParam("beans") int beans,
                                          @RequestParam("milk") int milk,
                                          @RequestParam("water") int water) {
        return coffeeMachineService.update(beans, milk, water);
    }

    @GetMapping("/status")
    public CoffeeMachine status() {
        return coffeeMachineService.status();
    }

    @GetMapping("/clean")
    public CoffeeMachine clean() {
        coffeeMachineService.clean();
        return status();
    }
}
