package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart fetchShoppingCartItemsByUserId(Long userId) {
        List<ShoppingCartItem> list = shoppingCartRepository.findByUserId(userId);
        return new ShoppingCart(list);
    }
}
