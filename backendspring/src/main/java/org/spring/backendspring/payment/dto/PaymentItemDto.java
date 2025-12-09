package org.spring.backendspring.payment.dto;

import lombok.*;
import org.spring.backendspring.payment.entity.PaymentItemEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentItemDto {

    private Long id;
    private Long itemId;
    private Integer price;
    private Integer size;
    private String title;
    private String s3file;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // Entity → DTO 변환
    public static PaymentItemDto fromEntity(PaymentItemEntity entity) {
        if (entity == null) return null;

        return PaymentItemDto.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .price(entity.getPrice())
                .size(entity.getSize())
                .title(entity.getTitle())
                .s3file(entity.getS3file())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    // DTO → Entity 변환
    public PaymentItemEntity toEntity() {
        return PaymentItemEntity.builder()
                .id(this.id)
                .itemId(this.itemId)
                .price(this.price)
                .size(this.size)
                .title(this.title)
                .s3file(this.s3file)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .build();
    }
}
