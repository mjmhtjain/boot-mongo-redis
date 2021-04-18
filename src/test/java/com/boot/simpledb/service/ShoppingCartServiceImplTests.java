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
        ShoppingCart cart = genCart(userId);

        Mockito.when(shoppingCartRepository.findByUserId(cart.userId))
                .thenReturn(cart);

        ShoppingCart actualResponse = shoppingCartServiceImpl
                .fetchShoppingCartItemsByUserId(cart.userId);

        assertEquals(cart, actualResponse);
    }

    @Test
    public void addItem_validData() {
        String userId = "1";
        ShoppingCart cart = genCart(userId);
        ShoppingCart expectedCart = genCart(userId);
        ShoppingCartItem item = new ShoppingCartItem("abc", "abc", 1);
        expectedCart.shoppingCartItems.add(item);

        Mockito.when(shoppingCartRepository.findByUserId(cart.userId))
                .thenReturn(cart);

        Mockito.when(shoppingCartRepository.save(expectedCart))
                .thenReturn(expectedCart);

        ShoppingCart actualResponse = shoppingCartServiceImpl
                .addItem(cart.userId, item);

        assertEquals(expectedCart, actualResponse);
    }

    private ShoppingCart genCart(String userId) {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();
        long userIdLongVal = Long.parseLong(userId);

        for (int i = 1; i <= 5; i++) {
            ShoppingCartItem item = new ShoppingCartItem(userId,
                    "Item" + i,
                    Long.valueOf(i));

            cartItemList.add(item);
        }

        return new ShoppingCart(userIdLongVal, cartItemList);
    }
}