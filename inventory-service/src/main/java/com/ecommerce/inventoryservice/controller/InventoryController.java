package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.InventoryRequest;
import com.ecommerce.inventoryservice.response.InventoryResponse;
import com.ecommerce.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
@Validated
public class InventoryController {

	private final InventoryService inventoryService;

	@GetMapping("/check")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> skuCode) {
		log.info("Checking stock for SKU codes: {}", skuCode);
		return ResponseEntity.ok(inventoryService.isInStock(skuCode));
	}

	@GetMapping("/{skuCode}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InventoryResponse> getInventoryBySkuCode(
			@PathVariable @NotBlank(message = "SKU code cannot be blank") String skuCode) {
		log.info("Fetching inventory for SKU code: {}", skuCode);
		return ResponseEntity.ok(inventoryService.getInventoryBySkuCode(skuCode));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<InventoryResponse>> getAllInventory() {
		log.info("Fetching all inventory");
		return ResponseEntity.ok(inventoryService.getAllInventory());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<InventoryResponse> createInventory(@RequestBody @Valid InventoryRequest request) {
		log.info("Creating inventory for SKU code: {}", request.getSkuCode());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(inventoryService.createInventory(request));
	}

	@PutMapping("/{skuCode}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InventoryResponse> updateInventory(
			@PathVariable @NotBlank(message = "SKU code cannot be blank") String skuCode,
			@RequestBody @Valid InventoryRequest request) {
		log.info("Updating inventory for SKU code: {}", skuCode);
		return ResponseEntity.ok(inventoryService.updateInventory(skuCode, request));
	}

	@DeleteMapping("/{skuCode}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteInventory(
			@PathVariable @NotBlank(message = "SKU code cannot be blank") String skuCode) {
		log.info("Deleting inventory for SKU code: {}", skuCode);
		inventoryService.deleteInventory(skuCode);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{skuCode}/reduce")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InventoryResponse> reduceInventory(
			@PathVariable @NotBlank(message = "SKU code cannot be blank") String skuCode,
			@RequestParam @NotNull(message = "Quantity cannot be null") 
			@Positive(message = "Quantity must be greater than zero") Integer quantity) {
		log.info("Reducing inventory for SKU code: {} by quantity: {}", skuCode, quantity);
		return ResponseEntity.ok(inventoryService.reduceInventory(skuCode, quantity));
	}

	@PostMapping("/{skuCode}/add")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InventoryResponse> addInventory(
			@PathVariable @NotBlank(message = "SKU code cannot be blank") String skuCode,
			@RequestParam @NotNull(message = "Quantity cannot be null") 
			@Positive(message = "Quantity must be greater than zero") Integer quantity) {
		log.info("Adding inventory for SKU code: {} by quantity: {}", skuCode, quantity);
		return ResponseEntity.ok(inventoryService.addInventory(skuCode, quantity));
	}
}
