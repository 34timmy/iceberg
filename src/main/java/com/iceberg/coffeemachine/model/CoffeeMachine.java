package com.iceberg.coffeemachine.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CoffeeMachine extends NamedEntity {

    private static final int DEFAULT_VALUE_FOR_CLEAN = 8;
    @NotNull
    @Value(value = "100")
    private int MAX_BEANS;
    @NotNull
    @Value(value = "2000")
    private int MAX_WATER;
    @NotNull
    @Value(value = "1000")
    private int MAX_MILK;

    @Value(value = "0")
    private int loadedBeans;
    @Value(value = "0")
    private int loadedWater;
    @Value(value = "0")
    private int loadedMilk;

    @Value(value = "8")
    private int cupsBeforeClean;

    private Map<CoffeeType, Coffee> coffeeMap = new HashMap<>();

    public void setDefaultCupsBeforeClean() {
        cupsBeforeClean = DEFAULT_VALUE_FOR_CLEAN;
    }

    @PostConstruct
    private void initCoffeeMachine() {
        name = "CM -1";
        loadedBeans = MAX_BEANS;
        loadedWater = MAX_WATER;
        loadedMilk = MAX_MILK;
        Coffee capuchino = new Coffee("Cap", CoffeeType.CAPUCHINO, 15, 100, 150);
        Coffee americano = new Coffee("Amer", CoffeeType.AMERICANO, 15, 0, 250);
        Coffee espresso = new Coffee("Espr", CoffeeType.ESPRESSO, 30, 0, 75);
        coffeeMap.put(CoffeeType.CAPUCHINO, capuchino);
        coffeeMap.put(CoffeeType.AMERICANO, americano);
        coffeeMap.put(CoffeeType.ESPRESSO, espresso);
    }

    @Override
    public String toString() {
        return "CoffeeMachine{" +
                "loadedBeans=" + loadedBeans +
                ", loadedWater=" + loadedWater +
                ", loadedMilk=" + loadedMilk +
                ", cupsBeforeClean=" + cupsBeforeClean +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}