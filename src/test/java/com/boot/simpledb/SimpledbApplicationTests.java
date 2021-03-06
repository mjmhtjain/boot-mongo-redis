package com.boot.simpledb;

import com.boot.simpledb.controller.ShoppingCartController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class SimpledbApplicationTests {

	@Autowired
	ShoppingCartController shoppingCartController;

	@Test
	void contextLoads() {
		assertNotNull(shoppingCartController);
	}

}
