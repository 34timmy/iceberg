package com.iceberg.coffeemachine;

import com.iceberg.coffeemachine.model.Coffee;
import com.iceberg.coffeemachine.model.CoffeeMachine;
import com.iceberg.coffeemachine.model.CoffeeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CoffeemachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeemachineApplication.class, args);
    }

    @PostConstruct
    private void init() {

    }
}
