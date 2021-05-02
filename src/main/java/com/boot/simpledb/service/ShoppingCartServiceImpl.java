package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRedisRepo;
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

    @Autowired
    ShoppingCartRedisRepo shoppingCartRedisRepo;

    @Override
    public ShoppingCart fetchShoppingCartItems(Long userId) {
        log.info("fetchShoppingCartItems: userId: {}", userId);

        ShoppingCart cart = shoppingCartRedisRepo.cacheGet(userId);
        if (cart == null) {
            log.info("Cache Miss, fetching from DB ..");

            cart = shoppingCartRepository.findByUserId(userId);
            if(cart != null){
                log.info("Fetching cart from DB");
                shoppingCartRedisRepo.cachePut(cart);
            }

        }

        return cart;
    }

    @Override
    public ShoppingCart addItem(long userId, ShoppingCartItem shoppingCartItem) {
        log.info("addItem: userId: {}, shoppingCartItem: {}", userId, shoppingCartItem);

        ShoppingCart shoppingCart = fetchShoppingCartItems(userId);
        if (shoppingCart == null) {
            shoppingCart = createShoppingCart(userId);
        }
        shoppingCart.shoppingCartItems.add(shoppingCartItem);

        ShoppingCart cart = shoppingCartRepository.save(shoppingCart);
        shoppingCartRedisRepo.cachePut(cart);

        return cart;
    }

    private ShoppingCart createShoppingCart(long userId) {
        log.info("createShoppingCart: userId: {}", userId);
        ShoppingCart shoppingCart = new ShoppingCart(userId);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeItem(long userIdLongVal, ShoppingCartItem shoppingCartItem) {
        return null;
    }
}
