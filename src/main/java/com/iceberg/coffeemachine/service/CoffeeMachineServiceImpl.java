package com.iceberg.coffeemachine.service;

import com.iceberg.coffeemachine.model.Coffee;
import com.iceberg.coffeemachine.model.CoffeeMachine;
import com.iceberg.coffeemachine.model.CoffeeType;
import com.iceberg.coffeemachine.util.CleanMachineException;
import com.iceberg.coffeemachine.util.NotEnoughException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoffeeMachineServiceImpl implements CoffeeMachineService {


    private int brewedCoffee = 0;

    private CoffeeMachine coffeeMachine;

    @Autowired
    public CoffeeMachineServiceImpl(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    @Override
    public ResponseEntity doCoffee(String coffeeTypeString) {
        CoffeeType coffeeType = CoffeeType.valueOf(coffeeTypeString);
        Coffee coffee = coffeeMachine.getCoffeeMap().get(coffeeType);
        try {
            isClean(coffeeMachine);
            isEnough(coffee);

            switch (coffeeType) {
                case ESPRESSO:
                    coffeeMachine.setCupsBeforeClean(coffeeMachine.getCupsBeforeClean() - 1);
                    break;
                case AMERICANO:
                    coffeeMachine.setCupsBeforeClean(coffeeMachine.getCupsBeforeClean() - 1);
                    break;
                case CAPUCHINO:
                    coffeeMachine.setCupsBeforeClean(coffeeMachine.getCupsBeforeClean() - 1);
                    break;
                default:
                    break;
            }
            log.info(String.format("%s(%s) prepared. Resources: beans = %d, milk = %d, water = %d.",
                    coffee.getName(), coffee.getCoffeeType(), coffeeMachine.getLoadedBeans(),
                    coffeeMachine.getLoadedMilk(), coffeeMachine.getLoadedWater()));
            return ResponseEntity.ok(coffee);
        } catch (NotEnoughException | CleanMachineException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity update(int beans, int milk, int water) {
        String msg = "";
        int beanLevel = coffeeMachine.getLoadedBeans() + beans - coffeeMachine.getMAX_BEANS();
        int waterLevel = coffeeMachine.getLoadedWater() + water - coffeeMachine.getMAX_WATER();
        int milkLevel = coffeeMachine.getLoadedMilk() + milk - coffeeMachine.getMAX_MILK();
        if (beanLevel > 0) {
            msg += notEnoughCapacity("bean", beanLevel);
        } else {
            coffeeMachine.setLoadedBeans(coffeeMachine.getLoadedBeans() + beans);
        }
        if (waterLevel > 0) {
            msg += notEnoughCapacity("water", waterLevel);
        } else {
            coffeeMachine.setLoadedWater(coffeeMachine.getLoadedWater() + water);
        }
        if (milkLevel > 0) {
            msg += notEnoughCapacity("milk", milkLevel);
        } else {
            coffeeMachine.setLoadedMilk(coffeeMachine.getLoadedMilk() + milk);
        }
        if (msg.isEmpty()) {
            return ResponseEntity.ok(coffeeMachine.toString());
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }

    }


    @Override
    public void isClean(CoffeeMachine coffeeMachine) throws CleanMachineException {
        if (coffeeMachine.getCupsBeforeClean() == 0) {
            throw new CleanMachineException(String.format("%s need to clean", coffeeMachine.getName()));
        }
    }

    public void clean() {
        coffeeMachine.setDefaultCupsBeforeClean();
    }


    public CoffeeMachine status() {
        return coffeeMachine;
    }

    @Override
    public void isEnough(Coffee coffee) throws NotEnoughException {
        String msg = "";
        int neededBeans = coffee.getBeansAmount();
        int neededMilk = coffee.getMilkAmount();
        int neededWater = coffee.getWaterAmount();

        int beansDiffer = coffeeMachine.getLoadedBeans() - neededBeans;
        int milkDiffer = coffeeMachine.getLoadedMilk() - neededMilk;
        int waterDiffer = coffeeMachine.getLoadedWater() - neededWater;

        if (waterDiffer < 0) {
            msg += notEnoughResourceMsg("water", coffeeMachine.getLoadedWater(), neededWater);
        }
        if (milkDiffer < 0) {
            msg += notEnoughResourceMsg("milk", coffeeMachine.getLoadedMilk(), neededMilk);
        }
        if (beansDiffer < 0) {
            msg += notEnoughResourceMsg("beans", coffeeMachine.getLoadedBeans(), neededBeans);
        }
        if (msg.isEmpty()) {
            coffeeMachine.setLoadedBeans(beansDiffer);
            coffeeMachine.setLoadedWater(waterDiffer);
            coffeeMachine.setLoadedMilk(milkDiffer);
        } else {
            throw new NotEnoughException(msg);
        }
    }

    private String notEnoughCapacity(String resource, int level) {
        return String.format("Capacity of %s's container is less on: %d.\n", resource, level);
    }

    private String notEnoughResourceMsg(String resource, int loaded, int needed) {
        return String.format(" - Current %1$s: %2$d mg/ ml.\n" +
                "   Needed %1$s: %3$d ml.\n" +
                "   Please fill the container.", resource, loaded, needed);

    }
}
