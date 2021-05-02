package com.boot.simpledb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String itemName;
    public long quantity;
}
