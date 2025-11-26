package com.ecommerce.inventoryservice.consumer;

import com.ecommerce.inventoryservice.dto.ProductEvent;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductEventConsumer {

	private final InventoryService inventoryService;

	@KafkaListener(topics = "product-events", groupId = "inventory-service-group")
	public void handleProductEvent(ProductEvent productEvent) {
		log.info("Received product event: {} for SKU: {}", productEvent.getEventType(), productEvent.getSku());
		
		try {
			switch (productEvent.getEventType()) {
				case "CREATED":
					inventoryService.handleProductCreated(productEvent.getSku(), productEvent.getQuantity());
					break;
				case "UPDATED":
					inventoryService.handleProductUpdated(productEvent.getSku(), productEvent.getQuantity());
					break;
				case "DELETED":
					inventoryService.handleProductDeleted(productEvent.getSku());
					break;
				default:
					log.warn("Unknown event type: {}", productEvent.getEventType());
			}
		} catch (Exception e) {
			log.error("Error processing product event for SKU: {}", productEvent.getSku(), e);
			// In a production environment, you might want to implement retry logic or dead letter queue
		}
	}
}

