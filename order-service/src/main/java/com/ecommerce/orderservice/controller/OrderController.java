package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.request.OrderRequest;
import com.ecommerce.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String placeOrder(@RequestBody OrderRequest orderRequest) {
		orderService.placeOrder(orderRequest);
		return "Order Placed successfully";
	}

}
