package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart fetchShoppingCartItemsByUserId(Long userId) {
        log.info("fetchShoppingCartItemsByUserId: ");

        List<ShoppingCartItem> list = shoppingCartRepository.findByUserId(userId);
        return new ShoppingCart(list);
    }

    @Override
    public ShoppingCartItem addItem(ShoppingCartItem shoppingCartItem) {
        log.info("addItem: ");

        return shoppingCartRepository.save(shoppingCartItem);
    }
}
