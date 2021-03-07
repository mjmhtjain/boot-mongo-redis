package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CARTITEM")
public class ShoppingCartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_seq")
    @SequenceGenerator(name = "cart_seq", sequenceName = "CARTITEMSEQ", allocationSize = 1)
    public Long id;

    @Column(name = "userid")
    public Long userId;

    @Column(name = "itemid")
    public Long itemId;

    @Column(name = "itemname")
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
