package com.github.oleksii.zinkevych.currency_exchange_point;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan({"com.github.oleksii.zinkevych.currency_exchange_point.entity"})
@EnableScheduling
@EnableFeignClients
public class CurrencyExchangePointApplication {

    @Configuration
    static class MainConfig {
        @Bean
        public ModelMapper modelMapper() {
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
            return mapper;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangePointApplication.class, args);
    }

}
