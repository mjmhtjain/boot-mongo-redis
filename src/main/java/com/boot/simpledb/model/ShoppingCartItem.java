package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem {
    public String id;
    public String itemName;
    public long quantity;
}
