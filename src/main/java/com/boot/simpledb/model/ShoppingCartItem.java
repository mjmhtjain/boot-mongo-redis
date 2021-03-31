package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ShoppingCart")
public class ShoppingCartItem implements Serializable {
    @Id
    public String id;

    public Long userId;

    public Long itemId;

    public String itemName;

    public Long quantity;

    public ShoppingCartItem(Long userId, Long itemId, String itemName, Long quantity) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }
}
