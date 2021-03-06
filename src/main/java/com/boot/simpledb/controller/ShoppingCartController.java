package com.boot.simpledb.controller;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.service.ShoppingCartService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCartController {
    Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping
    ResponseEntity demo() {
        log.info("demo: ");

        return ResponseEntity
                .ok()
                .body("Hello!!");
    }

    @GetMapping("/shoppingCart/{userId}")
    ResponseEntity shoppingCartItems(@PathVariable @NonNull String userId) {
        log.info("demo: userId: {}", userId);

        try {
            long userIdLongVal = Long.parseLong(userId);

            ShoppingCart res = shoppingCartService.fetchShoppingCartItemsByUserId(userIdLongVal);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(res);
        } catch (NumberFormatException exp) {
            log.error("userId conversion error: {}", exp);

            return ResponseEntity
                    .unprocessableEntity()
                    .build();
        }
    }
}
