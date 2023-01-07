package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import java.util.Map;

import com.github.oleksii.zinkevych.currency_exchange_point.controller.common.ExchangeController;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ExchangeControllerImpl implements ExchangeController {

    private final BankService bankService;

    public ResponseEntity<Map<String, ?>> updateExchanges() {
        bankService.getExchangeRates();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("status", HttpStatus.OK.value(),
                "success", true));
    }
}
