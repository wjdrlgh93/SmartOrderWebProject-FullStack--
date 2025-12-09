package org.spring.backendspring.cart.service;

import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {

    CartEntity getCartByMemberId(Long memberId);

    CartEntity createCart(Long memberId);

    CartItemEntity addItemToCart(Long cartId, Long itemId, int itemSize);

    List<CartItemEntity> getCartItems(Long cartId);

    void removeItem(Long cartItemId);

    Page<CartItemEntity> getCartItems(Long cartId, Pageable pageable);

    Page<CartItemEntity> searchCartItems(Long cartId, String keyword, Pageable pageable);

    CartItemEntity updateItemQuantity(Long cartItemId, int quantity);
}
