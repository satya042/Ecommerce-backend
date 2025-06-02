package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.response.InventoryResponse;
import com.ecommerce.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
//@RequiredArgsConstructor
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	//	@GetMapping("/{sku-code}")
	//	@ResponseStatus(HttpStatus.OK)
	//	public boolean isInStock(@PathVariable("sku-code") String skuCode) {
	//		return inventoryService.isInStock(skuCode);
	//	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
		return inventoryService.isInStock(skuCode);
	}
}
