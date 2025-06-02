package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {

		// return inventoryRepository.findBySkuCode(skuCode).isPresent();

		return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory -> InventoryResponse.builder()
				.skuCode(inventory.getSkuCode()).isInStock(inventory.getQuntity() > 0).build()).toList();
	}

}
