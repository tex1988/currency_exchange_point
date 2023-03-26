package com.github.oleksii.zinkevych.currency_exchange_point.controller.common;

import java.util.List;

import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationConfirmRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.request.ApplicationCreateRequest;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ApplicationModel;
import com.github.oleksii.zinkevych.currency_exchange_point.model.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Application API")
@ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = "application/json",
    schema = @Schema(implementation = ErrorResponse.class))})
@RequestMapping(value = "/application", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ApplicationController {

    @Operation(summary = "Find all applications", responses = {
        @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ApplicationModel.class)))}),
        @ApiResponse(responseCode = "409", description = "Conflict", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    List<ApplicationModel> getAll();

    @Operation(summary = "Find single application by Id", responses = {
        @ApiResponse(responseCode = "200", description = "Ok", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ApplicationModel.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "409", description = "Conflict", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    ApplicationModel get(
        @Parameter(description = "Application Id", required = true)
        @PathVariable Long id);

    @Operation(summary = "Create application", responses = {
        @ApiResponse(responseCode = "201", description = "Created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ApplicationModel.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "409", description = "Conflict", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    ApplicationModel createApplication(
        @Parameter(description = "New Application request payload", required = true)
        @Valid
        @RequestBody ApplicationCreateRequest applicationCreateRequest);

    @Operation(summary = "Create new application", responses = {
        @ApiResponse(responseCode = "200", description = "Confirmed", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ApplicationModel.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "409", description = "Conflict", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "422", description = "Not confirmed", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/confirm")
    ApplicationModel confirmApplication(
        @Parameter(description = "Confirm application after creation", required = true)
        @Valid
        @RequestBody ApplicationConfirmRequest confirmRequest);
}
