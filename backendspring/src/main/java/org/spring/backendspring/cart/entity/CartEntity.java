package org.spring.backendspring.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.cart.dto.CartDto;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.repository.ItemRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart_tb")
public class CartEntity extends BasicTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_Id")
    private Long id;

    private Long memberId;

    // 1:N
    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItemEntity> cartItemEntities = new ArrayList<>();

     //DTO 변환 메서드 추가 
    public CartDto toDto() {
        return CartDto.builder()
                .cartId(this.id)
                .memberId(this.memberId)
                .items(this.cartItemEntities != null
                        ? this.cartItemEntities.stream()
                            .map(CartItemEntity::toDto)
                            .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

   public static CartEntity fromDto(CartDto dto, ItemRepository itemRepository) {
    if (dto == null) return null;

    CartEntity cart = CartEntity.builder()
            .id(dto.getCartId())
            .memberId(dto.getMemberId())
            .build();

    if (dto.getItems() != null) {
        List<CartItemEntity> itemEntities = dto.getItems().stream()
                .map(itemDto -> {
                    ItemEntity itemEntity = itemRepository.findById(itemDto.getItemId())
                            .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
                    CartItemEntity cartItem = CartItemEntity.fromDto(itemDto, cart, itemEntity);
                    return cartItem;
                })
                .collect(Collectors.toList());
        cart.setCartItemEntities(itemEntities);
    }

    return cart;
}


}