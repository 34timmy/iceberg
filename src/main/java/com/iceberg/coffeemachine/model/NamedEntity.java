package com.iceberg.coffeemachine.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NamedEntity extends BaseEntity {

    @NotBlank
    protected String name;

    public NamedEntity(String name) {
        super();
        this.name = name;
    }
}