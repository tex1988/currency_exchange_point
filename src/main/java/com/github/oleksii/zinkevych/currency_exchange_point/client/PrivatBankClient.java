package com.github.oleksii.zinkevych.currency_exchange_point.client;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.entity.ExchangeRate;
import com.github.oleksii.zinkevych.currency_exchange_point.logger.FeignLogger;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "pb", url="${pb.url}",
    configuration = PrivatBankClient.PrivatBankClientConfig.class)
public interface PrivatBankClient {

    @Configuration
    class PrivatBankClientConfig {
        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> requestTemplate
                .query("json", "")
                .query("exchange", "")
                .query("coursid", "5");
        }

        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Bean
        public Logger logger() {
            return new FeignLogger();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pubinfo")
    List<ExchangeRate> getExchangeRates();
}
