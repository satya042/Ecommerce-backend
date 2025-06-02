package com.ecommerce.inventoryservice;

//import org.inventory.service.model.Inventory;
//import org.inventory.service.repository.InventoryRepository;
//import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);


    //	@Bean
    //	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
    //		return args -> {
    //			Inventory inventory = new Inventory();
    //			inventory.setSkuCode("Iphone_13");
    //			inventory.setQuntity(100000);
    //
    //			Inventory inventory1 = new Inventory();
    //			inventory.setSkuCode("Iphone_13");
    //			inventory.setQuntity(110000);
    //
    //			inventoryRepository.save(inventory);
    //			inventoryRepository.save(inventory1);
    //		};
    //	}
}
}
