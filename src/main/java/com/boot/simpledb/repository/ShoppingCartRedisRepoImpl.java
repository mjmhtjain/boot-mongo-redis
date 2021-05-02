package com.boot.simpledb.repository;

import com.boot.simpledb.model.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartRedisRepoImpl implements ShoppingCartRedisRepo {
    Logger log = LoggerFactory.getLogger(ShoppingCartRedisRepoImpl.class);

    @Autowired
    private RedisTemplate<Long, ShoppingCart> redisTemplate;

    @Override
    public ShoppingCart cacheGet(Long userId) {
        log.info("cacheGet: userId: {}", userId);

        return redisTemplate.opsForValue().get(userId);
    }

    @Override
    @Async("specificTaskExecutor")
    public void cachePut(ShoppingCart cart) {
        log.info("cachePut: cart: {}", cart);

        redisTemplate.opsForValue().set(cart.userId, cart);
    }
}
