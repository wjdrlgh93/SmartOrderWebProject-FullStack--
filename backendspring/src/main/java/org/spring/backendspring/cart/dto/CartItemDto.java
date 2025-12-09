package org.spring.backendspring.cart.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.item.dto.ItemImgDto;
import org.spring.backendspring.item.entity.ItemEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long cartItemId;
    private Long itemId;       // DB 연관용
    private int itemSize;

    // Item 정보 포함
    private String itemTitle;
    private int itemPrice;
    private String itemImage; // S3 URL 또는 newFileName

    private List<ItemImgDto> itemImgDtos;

    // Entity → DTO 변환
    public static CartItemDto fromEntity(CartItemEntity entity) {
        if (entity == null) return null;

        String image = null;
        if (entity.getItemEntity() != null && entity.getItemEntity().getItemImgEntities() != null
                && !entity.getItemEntity().getItemImgEntities().isEmpty()) {
            image = entity.getItemEntity().getItemImgEntities().get(0).getNewName();
        }

        List<ItemImgDto> imgDtoList = new ArrayList<>();
        if (entity.getItemEntity() != null && entity.getItemEntity().getItemImgEntities() != null) {
            imgDtoList = entity.getItemEntity().getItemImgEntities().stream()
                    .map(ItemImgDto::toItemImgDto) // ItemImgDto에 정의된 변환 메서드 사용 (이름 확인 필요: of, fromEntity 등)
                    .collect(Collectors.toList());
        }

        return CartItemDto.builder()
                .cartItemId(entity.getCartItemId())
                .itemId(entity.getItemEntity() != null ? entity.getItemEntity().getId() : null)
                .itemSize(entity.getItemSize())
                .itemTitle(entity.getItemEntity() != null ? entity.getItemEntity().getItemTitle() : null)
                .itemPrice(entity.getItemEntity() != null ? entity.getItemEntity().getItemPrice() : 0)
                .itemImage(image)
                .itemImgDtos(imgDtoList)
                .build();
    }

    // DTO → Entity 변환
    public CartItemEntity toEntity() {
        CartItemEntity entity = CartItemEntity.builder()
                .cartItemId(this.cartItemId)
                .itemSize(this.itemSize)
                .build();

        if (this.itemId != null) {
            ItemEntity item = new ItemEntity();
            item.setId(this.itemId);
            entity.setItemEntity(item);
        }

        return entity;
    }
}
