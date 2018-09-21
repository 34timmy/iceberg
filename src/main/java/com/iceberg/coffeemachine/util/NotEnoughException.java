package com.iceberg.coffeemachine.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotEnoughException extends Exception {

    public NotEnoughException(String message) {
        super(message);
        log.error(message);
    }
}
