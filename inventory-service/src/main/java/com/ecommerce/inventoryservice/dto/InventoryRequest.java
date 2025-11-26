package com.ecommerce.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {

	@NotBlank(message = "SKU code cannot be blank")
	private String skuCode;

	@NotNull(message = "Quantity cannot be null")
	@Min(value = 0, message = "Quantity must be greater than or equal to zero")
	private Integer quantity;
}

