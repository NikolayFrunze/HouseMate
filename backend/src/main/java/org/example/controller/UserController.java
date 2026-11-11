package org.example.controller;

import org.example.dto.CreateUserRequest;
import org.example.dto.LoginRequest;
import org.example.dto.UserResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for everything under the /users path.
 * It receives the HTTP requests from the Android app, calls the service
 * and returns the HTTP response (status code + JSON body).
 */
@Tag(name = "Users", description = "Registration and login")
@RestController
@RequestMapping("/users")
public class UserController {

    // Business logic used by the endpoints
    private final UserService userService;

    // Spring injects the service automatically
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /users
     * Registers a new user. Returns 201 (Created) with the new user data.
     * @Valid checks the validation annotations of CreateUserRequest first.
     */
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    @ApiResponse(responseCode = "409", description = "Email already registered", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * POST /users/login
     * Checks the email and password. Returns 200 with the user data if they are correct,
     * or 401 if not (the 401 comes from the exception handler below).
     */
    @Operation(summary = "Log in with email and password")
    @ApiResponse(responseCode = "200", description = "Credentials are correct")
    @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    @ApiResponse(responseCode = "401", description = "Wrong email or password", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    /**
     * Handles the error when the email is already registered.
     * Returns 409 (Conflict) with the error message as text.
     */
    @ExceptionHandler(UserService.EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailExists(UserService.EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles the error when the login data is wrong.
     * Returns 401 (Unauthorized) with the error message as text.
     */
    @ExceptionHandler(UserService.InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(UserService.InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
