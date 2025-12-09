package org.spring.backendspring.cart.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.cart.repository.CartRepository;
import org.spring.backendspring.cart.repository.CartItemRepository;
import org.spring.backendspring.cart.service.CartService;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // ⭐️ Optional 임포트 추가

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

   


    @Override
    @Transactional
    public CartEntity getCartByMemberId(Long memberId) {
        CartEntity cart = cartRepository.findByMemberId(memberId).orElse(null);
        if (cart != null) {
            cart.getCartItemEntities().size(); // 강제 로딩
        }
        return cart;
    }

    @Override
    public CartEntity createCart(Long memberId) {
        return cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(CartEntity.builder()
                        .memberId(memberId)
                        .build()));
    }

    @Override
    public CartItemEntity addItemToCart(Long cartId, Long itemId, int itemSize) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다!"));

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다!"));

        // ⭐️⭐️ [핵심 수정 시작] 동일 상품 존재 여부 확인 및 수량 누적 ⭐️⭐️
        // (주의: CartItemRepository에 findByCartEntity_IdAndItemEntity_Id 메서드가 정의되어 있어야 합니다.)
        Optional<CartItemEntity> existingItemOpt =
                cartItemRepository.findByCartEntity_IdAndItemEntity_Id(cartId, itemId);

        if (existingItemOpt.isPresent()) {
            // 1. 상품이 이미 존재하는 경우: 수량을 누적합니다.
            CartItemEntity existingItem = existingItemOpt.get();
            int newSize = existingItem.getItemSize() + itemSize;

            existingItem.setItemSize(newSize); // 수량 업데이트
            // @Transactional 덕분에 자동 저장되거나 (Dirty Checking), 명시적 save가 필요할 수 있습니다.
            // 여기서는 save 없이 트랜잭션 종료 시 반영되도록 처리합니다.

            return existingItem;

        } else {
            // 2. 상품이 존재하지 않는 경우: 새로운 CartItemEntity를 생성하고 저장합니다.
            CartItemEntity cartItem = CartItemEntity.builder()
                    .cartEntity(cart)
                    .itemSize(itemSize)
                    .itemEntity(itemEntity)
                    .build();

            CartItemEntity savedItem = cartItemRepository.save(cartItem);
            // CartEntity와 CartItemEntity 간의 양방향 매핑을 위해 컬렉션에 추가
            cart.getCartItemEntities().add(savedItem);

            return savedItem;
        }
        // ⭐️⭐️ [핵심 수정 끝] ⭐️⭐️
    }

    @Override
    public List<CartItemEntity> getCartItems(Long cartId) {
        return cartItemRepository.findByCartEntity_Id(cartId);
   

    }

    @Override
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public Page<CartItemEntity> getCartItems(Long cartId, Pageable pageable) {
        // return cartItemRepository.findByCartEntity_Id(cartId, pageable);
        return cartItemRepository.findCartItemsWithImagesByCartId(cartId, pageable);
    }

    @Override
    public Page<CartItemEntity> searchCartItems(Long cartId, String keyword, Pageable pageable) {

        return cartItemRepository.searchCartItemsWithImages(cartId, keyword, pageable);

        // return cartItemRepository.findByCartEntity_IdAndItemEntity_ItemTitleContainingIgnoreCase(
                // cartId, keyword, pageable);
    }

    // 수량 변경 구현
    @Override
    public CartItemEntity updateItemQuantity(Long cartItemId, int quantity) {
        CartItemEntity cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다: " + cartItemId));

        cartItem.setItemSize(quantity); 

        return cartItemRepository.save(cartItem); 
    }
}