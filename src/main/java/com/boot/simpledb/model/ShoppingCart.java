package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ShoppingCart")
public class ShoppingCart {
    @Id
    public String id;
    public long userId;
    public List<ShoppingCartItem> shoppingCartItems;

    public ShoppingCart(long userId, List<ShoppingCartItem> shoppingCartItems) {
        this.userId = userId;
        this.shoppingCartItems = shoppingCartItems;
    }
}
