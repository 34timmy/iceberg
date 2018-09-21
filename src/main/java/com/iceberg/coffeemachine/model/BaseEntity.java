package com.iceberg.coffeemachine.model;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BaseEntity {

    protected String id;

    protected BaseEntity() {
        this.id = (UUID.randomUUID().toString());
    }

}
