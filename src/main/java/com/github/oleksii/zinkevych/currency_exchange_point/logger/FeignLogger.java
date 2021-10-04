package com.github.oleksii.zinkevych.currency_exchange_point.logger;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignLogger extends Logger {

    @Override
    protected void log(String s, String s1, Object... objects) {
        LOGGER.debug(String.format(s + s1, objects));
    }
}
