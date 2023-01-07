package com.github.oleksii.zinkevych.currency_exchange_point.controller.common;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Exchange rates API")
@ApiResponse(responseCode = "500", description = "Internal server error")
@RequestMapping(value = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ExchangeController {

    @Operation(summary = "Updates exchange rates", responses = {
        @ApiResponse(responseCode = "200", description = "Successful", content = @Content(mediaType = "application/json",
            schema = @Schema(example = "TODO: Example")))})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update")
    ResponseEntity<Map<String, ?>> updateExchanges();
}
