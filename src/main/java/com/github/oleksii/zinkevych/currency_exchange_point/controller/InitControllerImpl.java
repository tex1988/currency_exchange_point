package com.github.oleksii.zinkevych.currency_exchange_point.controller;

import com.github.oleksii.zinkevych.currency_exchange_point.controller.common.InitController;
import com.github.oleksii.zinkevych.currency_exchange_point.service.common.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InitControllerImpl implements InitController {

    private final BankService bankService;

    public ResponseEntity<String> startWorkDay() {
        bankService.getExchangeRates();
        //return "{\"status\": 200, \"message\": \"Success\"}";
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body("Succsess");
    }
}
