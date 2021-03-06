package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart fetchShoppingCartItemsByUserId(Long userId);
}
