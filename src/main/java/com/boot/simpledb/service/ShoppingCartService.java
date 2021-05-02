package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;

public interface ShoppingCartService {
    ShoppingCart fetchShoppingCartItems(Long userId);
    ShoppingCart addItem(long userId, ShoppingCartItem shoppingCartItem);
    ShoppingCart removeItem(long userIdLongVal, ShoppingCartItem shoppingCartItem);
}
