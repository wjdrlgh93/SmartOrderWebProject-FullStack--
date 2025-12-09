package org.spring.backendspring.cart.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.dto.CartDto;
import org.spring.backendspring.cart.dto.CartItemDto;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.service.CartService;
import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JWTUtil jwtUtil;

    // JWT 기반 회원 장바구니 조회/생성
    @GetMapping("/me")
    public CartDto getMyCart(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        var claims = jwtUtil.validateToken(token);
        Long memberId = Long.valueOf(claims.get("id").toString());

        CartEntity cart = cartService.getCartByMemberId(memberId);
        if (cart == null) cart = cartService.createCart(memberId);

        return cart.toDto();
    }

    // 장바구니에 아이템 추가
    @PostMapping("/{cartId}/item")
    public CartItemDto addItem(@PathVariable("cartId") Long cartId,
                               @RequestParam("itemId") Long itemId,
                               @RequestParam("itemSize") int itemSize) {
        return cartService.addItemToCart(cartId, itemId, itemSize).toDto();
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/item/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // ✨ 수정: RESTful 응답 (204 No Content) 적용
    public void removeItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.removeItem(cartItemId);
    }

    // 장바구니 아이템 수량 변경
    @PutMapping("/item/{cartItemId}/quantity")
    public CartItemDto updateItemQuantity(
            @PathVariable("cartItemId") Long cartItemId,
            @RequestParam("quantity") int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        return cartService.updateItemQuantity(cartItemId, quantity).toDto();
    }

    // 장바구니 아이템 페이징 조회 (안정화)
    @GetMapping("/{cartId}/items")
    public PagedResponse<CartItemDto> getCartItems(
            @PathVariable("cartId") Long cartId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        Page<CartItemEntity> pageResult;

        if (keyword.isEmpty()) {
            pageResult = cartService.getCartItems(cartId, PageRequest.of(page, size));
        } else {
            pageResult = cartService.searchCartItems(cartId, keyword, PageRequest.of(page, size));
        }

        List<CartItemDto> dtos = pageResult.getContent()
                .stream()
                .map(CartItemEntity::toDto)
                .toList();

        return PagedResponse.<CartItemDto>builder()
                .content(dtos)
                .currentPage(pageResult.getNumber() + 1)
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .hasFirst(pageResult.getNumber() + 1 > 1)
                .hasLast(pageResult.getNumber() + 1 < pageResult.getTotalPages())
                .startPage(((pageResult.getNumber()) / 5) * 5 + 1)
                .endPage(Math.min(((pageResult.getNumber()) / 5) * 5 + 5, pageResult.getTotalPages()))
                .blockSize(5)
                .build();
    }
}