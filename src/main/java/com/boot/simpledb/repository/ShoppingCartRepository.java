package com.boot.simpledb.repository;

import com.boot.simpledb.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartItem, Long> {

    @Query("Select i from ShoppingCartItem i where i.userId = :userId")
    List<ShoppingCartItem> findByUserId(@Param("userId") long userId);
}