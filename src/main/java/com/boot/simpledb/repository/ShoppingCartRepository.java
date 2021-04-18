package com.boot.simpledb.repository;

import com.boot.simpledb.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

    ShoppingCart findByUserId(long userId);
}