package org.example.service;

import org.example.dto.CreateHouseRequest;
import org.example.dto.HouseResponse;
import org.example.entity.House;
import org.example.entity.HouseMember;
import org.example.entity.User;
import org.example.repository.HouseMemberRepository;
import org.example.repository.HouseRepository;
import org.example.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

/**
 * Business logic for houses: reading the house of a user and registering it.
 * The controller calls these methods; the repositories talk to the database.
 *
 * A user "belongs to" a house through a row in house_members.
 * When a user registers a house, he/she becomes its first member.
 */
@Service
public class HouseService {

    // Used to read and write houses in the database
    private final HouseRepository houseRepository;
    // Used to read and write the memberships (who lives in which house)
    private final HouseMemberRepository houseMemberRepository;
    // Used to check that the user exists before giving them a house
    private final UserRepository userRepository;
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final String alphabet = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";

    // Spring injects all the dependencies automatically through the constructor
    public HouseService(HouseRepository houseRepository,
                        HouseMemberRepository houseMemberRepository,
                        UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.houseMemberRepository = houseMemberRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns the house the user belongs to, or an empty Optional if the user is not a member of any house.
     */
    public java.util.Optional<HouseResponse> findHouse(Long userId) {
        return houseMemberRepository.findByUserId(userId)
                .map(membership -> toResponse(membership.getHouse()));
    }

    /**
     * Registers the house of a user and makes the user its first member.
     * Throws UserNotFoundException if the user does not exist.
     * Throws HouseAlreadyExistsException if the user already belongs to a house.
     */
    @Transactional
    public HouseResponse createHouse(Long userId, CreateHouseRequest request) {
        // The user must exist (foreign key target)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // First check: does this user already belong to a house?
        if (houseMemberRepository.existsByUserId(userId)) {
            throw new HouseAlreadyExistsException(userId);
        }

        String joinCodeHouse = generateJoinCode();
        while(houseRepository.existsByJoinCode(joinCodeHouse)) {
            joinCodeHouse = generateJoinCode();

        }
        House house = new House(
                user,
                request.address().trim(),
                request.postalCode().trim(),
                request.housingCode().trim(),
                joinCodeHouse.trim().toUpperCase()
        );

        try {
            House saved = houseRepository.save(house);
            // The creator is the first member of the house
            houseMemberRepository.save(new HouseMember(saved, user));
            return toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            // Two requests at the same time can both pass the check above.
            // The unique index on user_id is the real guarantee.
            throw new HouseAlreadyExistsException(userId);
        }
    }

    @Transactional
    public HouseResponse joinHouse(Long userId, String joinCode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

            if (houseMemberRepository.existsByUserId(userId)) {

            throw new HouseAlreadyExistsException(userId);

            }

        House house = houseRepository.findByJoinCode(joinCode.trim().toUpperCase()).orElseThrow(()->new InvalidCodeException());

        houseMemberRepository.save(new HouseMember(house, user));

        return toResponse(house);
    }

    private String generateJoinCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(alphabet.length());
            code.append(alphabet.charAt(index));
        }
        return code.toString();

    }

    // Converts the entity into the data sent to the client
    private HouseResponse toResponse(House house) {
        return new HouseResponse(
                house.getId(),
                house.getUser().getId(),
                house.getAddress(),
                house.getPostalCode(),
                house.getHousingCode(),
                house.getJoinCode()
        );
    }

    /**
     * Error thrown when the user already belongs to a house. Controller answers 409 (Conflict).
     */
    public static class HouseAlreadyExistsException extends RuntimeException {

        public HouseAlreadyExistsException(Long userId) {
            super("User " + userId + " already has a house assigned");
        }
    }

    /**
     * Error thrown when the user id does not exist. Controller answers 404 (Not Found).
     */
    public static class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(Long userId) {
            super("No user exists with id " + userId);
        }
    }

    public static class InvalidCodeException extends RuntimeException {

        public InvalidCodeException() {
            super("The joinCode is not correct");
        }
    }
}
