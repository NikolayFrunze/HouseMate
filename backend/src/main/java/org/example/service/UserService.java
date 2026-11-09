package org.example.service;

import org.example.dto.CreateUserRequest;
import org.example.dto.LoginRequest;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Business logic for users: registration and login.
 * The controller calls these methods; the repository talks to the database.
 */
@Service
public class UserService {

    // Used to read and write users in the database
    private final UserRepository userRepository;
    // Used to encrypt passwords and to compare them with the stored hash
    private final PasswordEncoder passwordEncoder;

    // Spring injects both dependencies automatically through the constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user.
     * Throws EmailAlreadyExistsException if the email is already registered.
     */
    public UserResponse createUser(CreateUserRequest request) {
        // Normalize the email so "Ana@Mail.com" and "ana@mail.com" are the same user
        String email = normalizeEmail(request.email());

        // First check: is this email already in the database?
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        // Build the entity. The password is encrypted before saving, never stored in plain text.
        User user = new User(
                request.username().trim(),
                email,
                passwordEncoder.encode(request.password())
        );

        try {
            // Save the user and return only the safe data (no password)
            User saved = userRepository.save(user);
            return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail());
        } catch (DataIntegrityViolationException e) {
            // Two requests at the same time can both pass the check above.
            // The unique index on the email column is the real guarantee.
            throw new EmailAlreadyExistsException(email);
        }
    }

    /**
     * Checks the credentials of a user.
     * Returns the user data if the email exists AND the password is correct.
     * Throws InvalidCredentialsException otherwise.
     * For security, it does not say which of the two was wrong.
     */
    public UserResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());

        User user = userRepository.findByEmail(email)
                // Compare the typed password with the stored BCrypt hash
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                // No user, or wrong password: throw the error
                .orElseThrow(InvalidCredentialsException::new);

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    // Removes spaces and converts to lowercase, so emails are compared in the same way
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Error thrown when someone tries to register an email that already exists.
     * The controller turns it into HTTP 409 (Conflict).
     */
    public static class EmailAlreadyExistsException extends RuntimeException {

        public EmailAlreadyExistsException(String email) {
            super("A user with the email " + email + " already exists");
        }
    }

    /**
     * Error thrown when the email does not exist or the password is wrong.
     * The controller turns it into HTTP 401 (Unauthorized).
     */
    public static class InvalidCredentialsException extends RuntimeException {

        public InvalidCredentialsException() {
            super("Incorrect email or password");
        }
    }
}
