package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ShoppingCart")
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    public String id;
    public long userId;
    public List<ShoppingCartItem> shoppingCartItems;

    public ShoppingCart(long userId, List<ShoppingCartItem> shoppingCartItems) {
        this.userId = userId;
        this.shoppingCartItems = shoppingCartItems;
    }

    public ShoppingCart(long userId) {
        this.userId = userId;
        this.shoppingCartItems = new ArrayList<>();
    }
}
