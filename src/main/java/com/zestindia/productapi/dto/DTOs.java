package com.zestindia.productapi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class DTOs {

	public record AuthRequest(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password) {
	}

	public record AuthResponse(String accessToken, String refreshToken, String tokenType) {
	}

	public record ProductRequest(@NotBlank @Size(max = 255) String productName,
			@NotBlank @Size(max = 100) String createdBy) {
	}

	public record ProductResponse(Long id, String productName, String createdBy, LocalDateTime createdOn,
			String modifiedBy, LocalDateTime modifiedOn) {
	}

	public record ItemRequest(

			@NotNull(message = "Quantity is required") @Positive(message = "Quantity must be greater than 0") Integer quantity

	) {
	}

	public record ItemResponse(Long id, Long productId, Integer quantity) {
	}
}
