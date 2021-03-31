package com.boot.simpledb.repository;

import com.boot.simpledb.model.ShoppingCartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCartItem, Long> {

    List<ShoppingCartItem> findByUserId(long userId);
}