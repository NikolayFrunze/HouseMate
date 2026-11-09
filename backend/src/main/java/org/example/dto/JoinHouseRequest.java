package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinHouseRequest(
        @NotBlank @Size(max = 6) String joinCode
) {
}
