package com.github.oleksii.zinkevych.currency_exchange_point.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Init API")
@ApiResponse(responseCode = "500", description = "Internal server error")
@RequestMapping(value = "/init", produces = MediaType.APPLICATION_JSON_VALUE)
public interface InitController {

    @Operation(summary = "Create new application", responses = {
        @ApiResponse(responseCode = "200", description = "Init successful", content = @Content(mediaType = "application/json",
            schema = @Schema(example = "TODO: Example")))})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/start")
    ResponseEntity<String> startWorkDay();
}
