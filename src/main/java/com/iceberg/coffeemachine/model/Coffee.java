package com.iceberg.coffeemachine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Component
public class Coffee extends NamedEntity {

    @NotNull
    private CoffeeType coffeeType;
    @NotNull
    private int beansAmount;
    @NotNull
    private int milkAmount;
    @NotNull
    private int waterAmount;


    public Coffee(String name, @NotNull CoffeeType coffeeType,
                  @NotNull int beansAmount, @NotNull int milkAmount, @NotNull int waterAmount) {
        super(name);
        this.coffeeType = coffeeType;
        this.beansAmount = beansAmount;
        this.milkAmount = milkAmount;
        this.waterAmount = waterAmount;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coffeeType=" + coffeeType +
                ", beansAmount=" + beansAmount +
                ", milkAmount=" + milkAmount +
                ", waterAmount=" + waterAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Coffee coffee = (Coffee) o;
        return coffeeType == coffee.coffeeType && name.equals(coffee.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getName(), coffeeType);
    }
}
