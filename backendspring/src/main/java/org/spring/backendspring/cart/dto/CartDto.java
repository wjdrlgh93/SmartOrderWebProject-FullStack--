package org.spring.backendspring.cart.dto;

import lombok.*;
import org.spring.backendspring.cart.entity.CartEntity;
import org.spring.backendspring.cart.entity.CartItemEntity;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private Long cartId;
    private Long memberId;
    private List<CartItemDto> items;  // CartItemDto 리스트로 변경

    //  Entity → DTO 변환
    public static CartDto fromEntity(CartEntity entity) {
        if (entity == null) return null;

        return CartDto.builder()
                .cartId(entity.getId())
                .memberId(entity.getMemberId())
                .items(entity.getCartItemEntities() != null
                        ? entity.getCartItemEntities().stream()
                                .map(CartItemDto::fromEntity)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }

    // DTO → Entity 변환
    public CartEntity toEntity() {
        CartEntity cart = CartEntity.builder()
                .id(this.cartId)
                .memberId(this.memberId)
                .build();

        if (this.items != null) {
            cart.setCartItemEntities(
                    this.items.stream()
                            .map(dto -> {
                                CartItemEntity itemEntity = dto.toEntity();
                                itemEntity.setCartEntity(cart); // 양방향 매핑
                                return itemEntity;
                            })
                            .collect(Collectors.toList())
            );
        }

        return cart;
    }
}
