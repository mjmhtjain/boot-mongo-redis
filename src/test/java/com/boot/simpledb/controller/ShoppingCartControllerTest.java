package com.boot.simpledb.controller;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Test
    public void shoppingCartItems_validValues_expectingCorrectResponse() throws Exception {
        ShoppingCart cart = mockShoppingCart();

        Mockito.when(shoppingCartService.fetchShoppingCartItemsByUserId(Long.parseLong("1")))
                .thenReturn(cart);

        this.mockMvc
                .perform(get("/api/v1/shoppingCart/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shoppingCartItems").isArray())
                .andExpect(jsonPath("$.shoppingCartItems.[0].itemName")
                        .value(cart.shoppingCartItems.get(0).itemName));
    }

    @Test
    public void shoppingCartItems_invalidUserId_expect406() throws Exception {
        ShoppingCart cart = mockShoppingCart();

        Mockito.when(shoppingCartService.fetchShoppingCartItemsByUserId(Long.parseLong("1")))
                .thenReturn(cart);

        this.mockMvc
                .perform(get("/api/v1/shoppingCart/{userId}", "someValue"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shoppingCartItems_nonExistingUser_expect204ContentNotFound() throws Exception {
        ShoppingCart cart = mockShoppingCart();
        long userId = 1;

        Mockito.when(shoppingCartService.fetchShoppingCartItemsByUserId(userId))
                .thenReturn(null);

        this.mockMvc
                .perform(get("/api/v1/shoppingCart/{userId}", userId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shoppingCartItems_validRequestBody_expect2xx() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem("2", "item2", 2);

        ShoppingCart expectedCart = mockShoppingCart();
        expectedCart.shoppingCartItems.add(item);

        Mockito.when(shoppingCartService.addItem(expectedCart.userId, item))
                .thenReturn(expectedCart);

        this.mockMvc
                .perform(post("/api/v1/item/{userId}", expectedCart.userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(item)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(expectedCart.userId))
                .andExpect(jsonPath("$.shoppingCartItems.[1].id").value(expectedCart.shoppingCartItems.get(1).id));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShoppingCart mockShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart.userId = 1;
        shoppingCart.shoppingCartItems = new ArrayList<>();
        shoppingCart.shoppingCartItems.add(new ShoppingCartItem("1", "", Long.parseLong("1")));

        return shoppingCart;
    }
}