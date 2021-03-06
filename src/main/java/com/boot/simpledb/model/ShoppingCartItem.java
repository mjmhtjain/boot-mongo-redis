package com.boot.simpledb.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CartItem")
public class ShoppingCartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "userId")
    public Long userId;

    @Column(name = "itemId")
    public Long itemId;

    @Column(name = "itemName")
    public String itemName;

    @Column(name = "quantity")
    public Long quantity;

    public ShoppingCartItem(Long userId, Long itemId, String itemName, Long quantity) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }
}
