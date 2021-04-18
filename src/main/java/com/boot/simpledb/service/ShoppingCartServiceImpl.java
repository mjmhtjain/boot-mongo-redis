package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart fetchShoppingCartItemsByUserId(Long userId) {
        log.info("fetchShoppingCartItemsByUserId: ");

        return shoppingCartRepository.findByUserId(userId);
    }

    @Override
    public ShoppingCart addItem(long userId, ShoppingCartItem shoppingCartItem) {
        log.info("addItem: ");

        ShoppingCart shoppingCart = fetchShoppingCartItemsByUserId(userId);
        shoppingCart.shoppingCartItems.add(shoppingCartItem);

        return shoppingCartRepository.save(shoppingCart);
    }
}
