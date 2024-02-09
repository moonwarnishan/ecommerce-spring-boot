package com.brainstation23.ecommerce.ecommerce.controller.web;

import com.brainstation23.ecommerce.ecommerce.exception.custom.NotFoundException;
import com.brainstation23.ecommerce.ecommerce.mapper.CartItemMapper;
import com.brainstation23.ecommerce.ecommerce.model.domain.Address;
import com.brainstation23.ecommerce.ecommerce.model.domain.CartItem;
import com.brainstation23.ecommerce.ecommerce.model.domain.OrderItem;
import com.brainstation23.ecommerce.ecommerce.model.domain.User;
import com.brainstation23.ecommerce.ecommerce.model.dto.cartItem.Cart;
import com.brainstation23.ecommerce.ecommerce.model.dto.cartItem.CartItemCreateUpdateRequest;
import com.brainstation23.ecommerce.ecommerce.model.dto.order.OrderCreateRequest;
import com.brainstation23.ecommerce.ecommerce.persistence.entity.UserEntity;
import com.brainstation23.ecommerce.ecommerce.service.interfaces.CartItemService;
import com.brainstation23.ecommerce.ecommerce.service.interfaces.OrderService;
import com.brainstation23.ecommerce.ecommerce.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.brainstation23.ecommerce.ecommerce.controller.web.UserUpdateAndDetailsController.USER_DETAILS;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/cart")
public class CartController {

    private static final String USER_BASE = "user/base";
    private static final String ATTRIBUTE_PAGE_TITLE = "pageTitle";
    private static final String ATTRIBUTE_CONTENT = "content";

    private static final String CART_ATTRIBUTE = "cart";

    private final CartItemMapper cartItemMapper;

    private final UserService userService;

    private final CartItemService cartItemService;

    private final OrderService orderService;


    @GetMapping
    public String allCartItems(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer, Model model) {
        String redirectUrl = "/landingpage";
        model.addAttribute("previousUrl", redirectUrl);
        model.addAttribute(ATTRIBUTE_PAGE_TITLE, "Cart");
        model.addAttribute(ATTRIBUTE_CONTENT, "user/usercrud/cart");

        UserEntity userSession = userService.getSessionUser();
        if (userSession == null) throw new NotFoundException("Please Log in First");

        User user = userService.getOne(userSession.getId());
        var cartItemList = user.getCartItems();
        var cartItemResponseList = cartItemList.stream().map(cartItemMapper::domainToResponse).toList();

        Cart cart = new Cart().setCartItemList(cartItemResponseList);
        model.addAttribute(CART_ATTRIBUTE, cart);
        return USER_BASE;
    }

    @PostMapping("/update")
    public String updateCartItem(@ModelAttribute CartItemCreateUpdateRequest updatedItem) {
        updatedItem.setDate(new Timestamp(System.currentTimeMillis()));
        cartItemService.updateOne(updatedItem);
        return "redirect:/user/cart";
    }

    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable(value = "id") UUID id) {
        cartItemService.deleteOne(id);
        return "redirect:/user/cart";
    }

    @PostMapping("/place_order")
    public String placeOrder() {
        UserEntity userSession = userService.getSessionUser();

        User user = userService.getOne(userSession.getId());
        var cartItemList = user.getCartItems();

        Set<OrderItem> orderItems = getOrderItems(cartItemList);
        Address dummyAddress = getDummyDeliveryAddress();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest
                .setUser(user)
                .setItems(orderItems)
                .setDeliveryAddress(dummyAddress)
                .setTotalAmount(BigDecimal.valueOf(1200));

        orderService.createOne(orderCreateRequest);

        return "redirect:" + USER_DETAILS + "/getorders";
    }

    private Set<OrderItem> getOrderItems(List<CartItem> cartItems){
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(cartItem.getProduct())
                    .setQuantity(cartItem.getQuantity())
                    .setUnitPrice(cartItem.getProduct().getUnitPrice());

            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private Address getDummyDeliveryAddress(){
        return new Address()
                .setDetails("This is Dummy Address Details")
                .setZipCode("Dummy Zip Code");
    }

}
