package com.boot.simpledb.controller;

import com.boot.simpledb.model.ShoppingCart;
import com.boot.simpledb.model.ShoppingCartItem;
import com.boot.simpledb.service.ShoppingCartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void demo() throws Exception {
        this.mockMvc
                .perform(get("/api/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello!!"));
    }

    @Test
    public void shoppingCartItems_validValues_expectingCorrectResponse() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem(Long.parseLong("1"),
                Long.parseLong("1"),
                "",
                Long.parseLong("1"));
        ShoppingCart cart = new ShoppingCart(Arrays.asList(item));

        Mockito.when(shoppingCartService.fetchShoppingCartItemsByUserId(Long.parseLong("1")))
                .thenReturn(cart);

        this.mockMvc
                .perform(get("/api/v1/shoppingCart/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shoppingCartItems").isArray())
                .andExpect(jsonPath("$.shoppingCartItems.[0].itemName").value(item.itemName));
    }

    @Test
    public void shoppingCartItems_invalidUserId_expect406() throws Exception {
        ShoppingCartItem item = new ShoppingCartItem(Long.parseLong("1"),
                Long.parseLong("1"),
                "",
                Long.parseLong("1"));
        ShoppingCart cart = new ShoppingCart(Arrays.asList(item));

        Mockito.when(shoppingCartService.fetchShoppingCartItemsByUserId(Long.parseLong("1")))
                .thenReturn(cart);

        this.mockMvc
                .perform(get("/api/v1/shoppingCart/{userId}", "someValue"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}