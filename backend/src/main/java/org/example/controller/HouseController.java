package org.example.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.CreateHouseRequest;
import org.example.dto.HouseResponse;
import org.example.dto.JoinHouseRequest;
import org.example.service.HouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the house of a user.
 * Endpoints used by the Android app (HouseApi.java):
 *   GET  /users/{userId}/house  -> 200 with the house, or 404 if the user has none
 *   POST /users/{userId}/house  -> 201 with the saved house, or 409 if it already exists
 */
@Tag(name = "Houses", description = "House of a user: view, create and join")
@RestController
@RequestMapping("/users/{userId}/house")
public class HouseController {

    // Business logic used by the endpoints
    private final HouseService houseService;

    // Spring injects the service automatically
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    /**
     * GET /users/{userId}/house
     * Returns the house of the user (200), or 404 if the user has no house.
     */
    @Operation(summary = "Get the house of a user")
    @ApiResponse(responseCode = "200", description = "House found")
    @ApiResponse(responseCode = "404", description = "User has no house", content = @Content)
    @GetMapping
    public ResponseEntity<HouseResponse> getHouse(@PathVariable Long userId) {
        return houseService.findHouse(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /users/{userId}/house
     * Registers the house of the user. Returns 201 (Created) with the saved house.
     * @Valid checks the validation annotations of CreateHouseRequest first.
     */
    @Operation(summary = "Create a house for a user")
    @ApiResponse(responseCode = "201", description = "House created")
    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "409", description = "User already has a house", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @PostMapping
    public ResponseEntity<HouseResponse> createHouse(@PathVariable Long userId,
                                                     @Valid @RequestBody CreateHouseRequest request) {
        HouseResponse created = houseService.createHouse(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Join an existing house with its join code")
    @ApiResponse(responseCode = "201", description = "Joined the house")
    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found or invalid join code", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "409", description = "User already has a house", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @PostMapping("/join")
    public ResponseEntity<HouseResponse> joinHouse(@PathVariable Long userId,@Valid @RequestBody JoinHouseRequest request) {
        HouseResponse created = houseService.joinHouse(userId, request.joinCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Handles the error when the user already has a house.
     * Returns 409 (Conflict) with the error message as text.
     */
    @ExceptionHandler(HouseService.HouseAlreadyExistsException.class)
    public ResponseEntity<String> handleHouseExists(HouseService.HouseAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles the error when the user id does not exist.
     * Returns 404 (Not Found) with the error message as text.
     */
    @ExceptionHandler(HouseService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(HouseService.UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles the error when no house has the join code sent by the user.
     * Returns 404 (Not Found) with the error message as text.
     */
    @ExceptionHandler(HouseService.InvalidCodeException.class)
    public ResponseEntity<String> handleInvalidCode(HouseService.InvalidCodeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
