package com.ecommerce.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
	private UUID productId;
	private String sku;
	private String productTitle;
	private Integer quantity;
	private String eventType; // CREATED, UPDATED, DELETED
}

