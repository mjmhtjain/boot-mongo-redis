package com.boot.simpledb.service;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.repository.ShoppingCartRedisRepo;
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

    @Mock
    ShoppingCartRedisRepo shoppingCartRedisRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchShoppingCartItems_validData_cacheHit_expectValidResponse() {
        String userId = "1";
        ShoppingCart cart = genCart(userId);

        Mockito.when(shoppingCartRedisRepo.cacheGet(cart.userId))
                .thenReturn(cart);

        ShoppingCart actualResponse = shoppingCartServiceImpl
                .fetchShoppingCartItems(cart.userId);

        assertEquals(cart, actualResponse);
        Mockito.verifyZeroInteractions(shoppingCartRepository);
    }

    @Test
    public void fetchShoppingCartItems_validData_cacheMiss_expectValidResponse() {
        String userId = "1";
        ShoppingCart cart = genCart(userId);

        Mockito.when(shoppingCartRedisRepo.cacheGet(cart.userId))
                .thenReturn(null);

        Mockito.when(shoppingCartRepository.findByUserId(cart.userId))
                .thenReturn(cart);

        ShoppingCart actualResponse = shoppingCartServiceImpl
                .fetchShoppingCartItems(cart.userId);

        assertEquals(cart, actualResponse);
        Mockito.verify(shoppingCartRedisRepo).cachePut(cart);
    }

    @Test
    public void addItem_validData_goodResponse() {
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
        Mockito.verify(shoppingCartRedisRepo, Mockito.times(2))
                .cachePut(Mockito.any());
    }

    private ShoppingCart genCart(String userId) {
        List<ShoppingCartItem> cartItemList = new ArrayList<>();
        long userIdLongVal = Long.parseLong(userId);

        for (int i = 1; i <= 1; i++) {
            ShoppingCartItem item = new ShoppingCartItem(userId,
                    "Item" + i,
                    Long.valueOf(i));

            cartItemList.add(item);
        }

        return new ShoppingCart(userIdLongVal, cartItemList);
    }
}