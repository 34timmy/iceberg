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
@Service
public class CoffeeMachineServiceImpl implements CoffeeMachineService {

    private CoffeeMachine coffeeMachine;

    @Autowired
    public CoffeeMachineServiceImpl(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    /**
     * create coffee
     *
     * @param coffeeTypeString coffee type
     * @return if ok -> response with CoffeeMachine in body, else -> response with error message
     */
    @Override
    public ResponseEntity doCoffee(String coffeeTypeString) {
        CoffeeType coffeeType = CoffeeType.valueOf(coffeeTypeString);
        Coffee coffee = coffeeMachine.getCoffeeMap().get(coffeeType);
        try {
            isClean(coffeeMachine);
            isEnough(coffee);

            switch (coffeeType) {
                case ESPRESSO:
//                    some logic
                    coffeeMachine.setCupsBeforeClean(coffeeMachine.getCupsBeforeClean() - 1);
                    break;
                case AMERICANO:
//                    some logic
                    coffeeMachine.setCupsBeforeClean(coffeeMachine.getCupsBeforeClean() - 1);
                    break;
                case CAPUCHINO:
//                    some logic
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

    /**
     * update resources of coffee machine
     *
     * @param beans - quantity to be loaded
     * @param milk  - quantity to be loaded
     * @param water - quantity to be loaded
     * @return if ok -> response with CoffeeMachine in body, else -> response with error message
     */
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
            return ResponseEntity.ok(coffeeMachine);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }

    }

    /**
     * check remaining cups before cleaning
     *
     * @param coffeeMachine
     * @throws CleanMachineException if remaining cups = 0
     */
    @Override
    public void isClean(CoffeeMachine coffeeMachine) throws CleanMachineException {
        if (coffeeMachine.getCupsBeforeClean() == 0) {
            throw new CleanMachineException(String.format("%s need to clean", coffeeMachine.getName()));
        }
    }

    /**
     * set cups before cleaning to default value
     */
    public void clean() {
        coffeeMachine.setDefaultCupsBeforeClean();
    }

    /**
     * status of CoffeeMachine
     */
    public CoffeeMachine status() {
        return coffeeMachine;
    }

    /**
     * check is it enough resources for coffee producing
     *
     * @param coffee
     * @throws NotEnoughException if not enough resources for coffee producing
     */
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

    /**
     * template message for 'update' method
     *
     * @param resource updated resource
     * @param level    exceeding number of resources
     * @return String
     */
    private String notEnoughCapacity(String resource, int level) {
        return String.format("Capacity of %s's container is less on: %d.\n", resource, level);
    }

    /**
     * template for 'isEnough' method
     *
     * @param resource checked resource
     * @param loaded   resource quantity in coffee machine
     * @param needed   resource quantity needed for producing coffee
     * @return String
     */
    private String notEnoughResourceMsg(String resource, int loaded, int needed) {
        return String.format(" - Current %1$s: %2$d mg/ ml.\n" +
                "   Needed %1$s: %3$d ml.\n" +
                "   Please fill the container.", resource, loaded, needed);

    }
}
