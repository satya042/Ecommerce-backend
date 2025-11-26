package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryRequest;
import com.ecommerce.inventoryservice.exception.InventoryNotFoundException;
import com.ecommerce.inventoryservice.model.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.response.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		log.info("Checking stock for SKU codes: {}", skuCode);
		return inventoryRepository.findBySkuCodeIn(skuCode).stream()
				.map(inventory -> InventoryResponse.builder()
						.skuCode(inventory.getSkuCode())
						.quantity(inventory.getQuantity())
						.isInStock(inventory.getQuantity() > 0)
						.build())
				.toList();
	}

	@Transactional(readOnly = true)
	public InventoryResponse getInventoryBySkuCode(String skuCode) {
		log.info("Fetching inventory for SKU code: {}", skuCode);
		Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));
		return mapToResponse(inventory);
	}

	@Transactional(readOnly = true)
	public List<InventoryResponse> getAllInventory() {
		log.info("Fetching all inventory");
		return inventoryRepository.findAll().stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Transactional
	public InventoryResponse createInventory(InventoryRequest request) {
		log.info("Creating inventory for SKU code: {}", request.getSkuCode());
		
		// Check if inventory already exists
		Optional<Inventory> existingInventory = inventoryRepository.findBySkuCode(request.getSkuCode());
		if (existingInventory.isPresent()) {
			throw new IllegalArgumentException("Inventory already exists for SKU code: " + request.getSkuCode());
		}

		Inventory inventory = Inventory.builder()
				.skuCode(request.getSkuCode())
				.quantity(request.getQuantity())
				.build();

		Inventory savedInventory = inventoryRepository.save(inventory);
		log.info("Inventory created successfully for SKU code: {}", savedInventory.getSkuCode());
		return mapToResponse(savedInventory);
	}

	@Transactional
	public InventoryResponse updateInventory(String skuCode, InventoryRequest request) {
		log.info("Updating inventory for SKU code: {}", skuCode);
		Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));

		inventory.setQuantity(request.getQuantity());
		Inventory updatedInventory = inventoryRepository.save(inventory);
		log.info("Inventory updated successfully for SKU code: {}", updatedInventory.getSkuCode());
		return mapToResponse(updatedInventory);
	}

	@Transactional
	public void deleteInventory(String skuCode) {
		log.info("Deleting inventory for SKU code: {}", skuCode);
		Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));
		inventoryRepository.delete(inventory);
		log.info("Inventory deleted successfully for SKU code: {}", skuCode);
	}

	@Transactional
	public InventoryResponse reduceInventory(String skuCode, Integer quantity) {
		log.info("Reducing inventory for SKU code: {} by quantity: {}", skuCode, quantity);
		Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));

		if (inventory.getQuantity() < quantity) {
			throw new IllegalArgumentException("Insufficient inventory. Available: " + inventory.getQuantity() + ", Requested: " + quantity);
		}

		inventory.setQuantity(inventory.getQuantity() - quantity);
		Inventory updatedInventory = inventoryRepository.save(inventory);
		log.info("Inventory reduced successfully for SKU code: {}. Remaining quantity: {}", 
				updatedInventory.getSkuCode(), updatedInventory.getQuantity());
		return mapToResponse(updatedInventory);
	}

	@Transactional
	public InventoryResponse addInventory(String skuCode, Integer quantity) {
		log.info("Adding inventory for SKU code: {} by quantity: {}", skuCode, quantity);
		Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
				.orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));

		inventory.setQuantity(inventory.getQuantity() + quantity);
		Inventory updatedInventory = inventoryRepository.save(inventory);
		log.info("Inventory added successfully for SKU code: {}. New quantity: {}", 
				updatedInventory.getSkuCode(), updatedInventory.getQuantity());
		return mapToResponse(updatedInventory);
	}

	@Transactional
	public void handleProductCreated(String sku, Integer quantity) {
		log.info("Handling product created event for SKU: {} with quantity: {}", sku, quantity);
		Optional<Inventory> existingInventory = inventoryRepository.findBySkuCode(sku);
		
		if (existingInventory.isPresent()) {
			log.warn("Inventory already exists for SKU: {}. Updating quantity.", sku);
			Inventory inventory = existingInventory.get();
			inventory.setQuantity(quantity);
			inventoryRepository.save(inventory);
		} else {
			Inventory inventory = Inventory.builder()
					.skuCode(sku)
					.quantity(quantity != null ? quantity : 0)
					.build();
			inventoryRepository.save(inventory);
			log.info("Inventory created for new product with SKU: {}", sku);
		}
	}

	@Transactional
	public void handleProductUpdated(String sku, Integer quantity) {
		log.info("Handling product updated event for SKU: {} with quantity: {}", sku, quantity);
		Optional<Inventory> existingInventory = inventoryRepository.findBySkuCode(sku);
		
		if (existingInventory.isPresent()) {
			Inventory inventory = existingInventory.get();
			inventory.setQuantity(quantity != null ? quantity : inventory.getQuantity());
			inventoryRepository.save(inventory);
			log.info("Inventory updated for SKU: {}", sku);
		} else {
			log.warn("Inventory not found for SKU: {}. Creating new inventory.", sku);
			Inventory inventory = Inventory.builder()
					.skuCode(sku)
					.quantity(quantity != null ? quantity : 0)
					.build();
			inventoryRepository.save(inventory);
		}
	}

	@Transactional
	public void handleProductDeleted(String sku) {
		log.info("Handling product deleted event for SKU: {}", sku);
		Optional<Inventory> existingInventory = inventoryRepository.findBySkuCode(sku);
		
		if (existingInventory.isPresent()) {
			inventoryRepository.delete(existingInventory.get());
			log.info("Inventory deleted for SKU: {}", sku);
		} else {
			log.warn("Inventory not found for SKU: {}. Nothing to delete.", sku);
		}
	}

	private InventoryResponse mapToResponse(Inventory inventory) {
		return InventoryResponse.builder()
				.skuCode(inventory.getSkuCode())
				.quantity(inventory.getQuantity())
				.isInStock(inventory.getQuantity() > 0)
				.build();
	}
}
