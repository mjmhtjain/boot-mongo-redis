package com.boot.simpledb.repository;

import com.boot.simpledb.model.ShoppingCart;

public interface ShoppingCartRedisRepo {
    ShoppingCart cacheGet(Long userId);

    void cachePut(ShoppingCart cart);
}
