package org.spring.backendspring.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendspring.cart.dto.CartItemDto;
import org.spring.backendspring.item.entity.ItemEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_item_tb")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    private int itemSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") 
    private ItemEntity itemEntity;

    // DTO 변환
    public CartItemDto toDto() {
        String image = null;
        if (itemEntity != null && itemEntity.getItemImgEntities() != null
                && !itemEntity.getItemImgEntities().isEmpty()) {
            image = itemEntity.getItemImgEntities().get(0).getNewName();
        }

        return CartItemDto.builder()
                .cartItemId(this.cartItemId)
                .itemId(itemEntity != null ? itemEntity.getId() : null)
                .itemSize(this.itemSize)
                .itemTitle(itemEntity != null ? itemEntity.getItemTitle() : null)
                .itemPrice(itemEntity != null ? itemEntity.getItemPrice() : 0)
                .itemImage(image)
                .build();
    }

    public static CartItemEntity fromDto(CartItemDto dto, CartEntity cart, ItemEntity itemEntity) {
        if (dto == null) return null;

        return CartItemEntity.builder()
                .cartItemId(dto.getCartItemId())
                .itemSize(dto.getItemSize())
                .cartEntity(cart)
                .itemEntity(itemEntity)
                .build();
    }
}
