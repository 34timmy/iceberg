package com.iceberg.coffeemachine.service;

import com.iceberg.coffeemachine.model.Coffee;
import com.iceberg.coffeemachine.model.CoffeeMachine;
import com.iceberg.coffeemachine.util.CleanMachineException;
import com.iceberg.coffeemachine.util.NotEnoughException;
import org.springframework.http.ResponseEntity;

public interface CoffeeMachineService {

    ResponseEntity doCoffee(String coffeeTypeString);

    ResponseEntity update(int beans, int milk, int water);

    void isClean(CoffeeMachine coffeeMachine) throws CleanMachineException;

    void isEnough(Coffee coffee) throws NotEnoughException;

    CoffeeMachine status();

    void clean();
}
