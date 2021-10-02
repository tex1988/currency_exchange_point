package com.github.oleksii.zinkevych.currency_exchange_point.controller.common;

import javax.validation.Valid;

import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationConfirmRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Application API")
@ApiResponse(responseCode = "500", description = "Internal server error")
@RequestMapping(value = "/application", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ApplicationController {

    @Operation(summary = "Create application", responses = {
        @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
        schema = @Schema(implementation = ApplicationResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
            schema = @Schema(example = "TODO: Example"))),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(
            schema = @Schema(example = "TODO: Example"))),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    ApplicationResponse createApplication(
        @Parameter(description = "New Application request payload", required = true)
        @Valid
        @RequestBody ApplicationCreateRequest applicationCreateRequest);

    @Operation(summary = "Create new application", responses = {
        @ApiResponse(responseCode = "200", description = "Confirmed", content = @Content(
            schema = @Schema(example = "TODO: Example"))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(
            schema = @Schema(example = "TODO: Example"))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(
            schema = @Schema(example = "TODO: Example"))),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(
            schema = @Schema(example = "TODO: Example")))
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/confirm")
    void confirmApplication(
        @Parameter(description = "Confirm application after creation", required = true)
        @Valid
        @RequestBody ApplicationConfirmRequest confirmRequest);
}
