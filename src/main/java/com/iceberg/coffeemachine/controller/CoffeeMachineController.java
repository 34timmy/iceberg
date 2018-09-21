package com.iceberg.coffeemachine.controller;

import com.iceberg.coffeemachine.model.CoffeeMachine;
import com.iceberg.coffeemachine.service.CoffeeMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CoffeeMachineController {


    private CoffeeMachineService coffeeMachineService;

    @Autowired
    public CoffeeMachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    /**
     * create coffee
     * @param coffeeType - type of coffee (americano, espresso, capuchino)
     * @return if ok -> response with Coffee in body, else -> response with error message
     */
    @GetMapping("/do")
    public ResponseEntity getCoffee(@RequestParam("coffeeType") String coffeeType) {
        return coffeeMachineService.doCoffee(coffeeType.toUpperCase());
    }

    /**
     * update resources of coffee machine
     * @param beans - quantity to be loaded
     * @param milk - quantity to be loaded
     * @param water - quantity to be loaded
     * @return if ok -> response with CoffeeMachine in body, else -> response with error message
     */
    @GetMapping(value = "/update")
    public ResponseEntity updateResources(@RequestParam("beans") int beans,
                                          @RequestParam("milk") int milk,
                                          @RequestParam("water") int water) {
        return coffeeMachineService.update(beans, milk, water);
    }

    /**
     * status of coffee machine
     * @return CoffeeMachine object
     */
    @GetMapping("/status")
    public CoffeeMachine status() {
        return coffeeMachineService.status();
    }

    /**
     * set cups remaining before cleaning to default value
     * @return CoffeeMachine object
     */
    @GetMapping("/clean")
    public CoffeeMachine clean() {
        coffeeMachineService.clean();
        return status();
    }
}
