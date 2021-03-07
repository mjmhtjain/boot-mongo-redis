package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ShoppingCartServiceImplTests {

    @InjectMocks
    ShoppingCartServiceImpl shoppingCartServiceImpl;

    @Mock
    ShoppingCartRepository shoppingCartRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchShoppingCartItemsByUserId_validData_expectGoodResponse() {
        String userId = "1";
        long userIdLongVal = Long.parseLong(userId);
        List<ShoppingCartItem> itemList = genCartItemList(userId);
        ShoppingCart expectedResponse = new ShoppingCart(itemList);

        Mockito.when(shoppingCartRepository.findByUserId(userIdLongVal))
                .thenReturn(itemList);

        ShoppingCart actualResponse = shoppingCartServiceImpl
                .fetchShoppingCartItemsByUserId(userIdLongVal);

        assertEquals(expectedResponse, actualResponse);
    }

    private List<ShoppingCartItem> genCartItemList(String userId) {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            ShoppingCartItem item = new ShoppingCartItem(Long.parseLong(userId),
                    Long.valueOf(i),
                    "Item" + i,
                    Long.valueOf(i));

            cartItemList.add(item);
        }

        return cartItemList;
    }
}