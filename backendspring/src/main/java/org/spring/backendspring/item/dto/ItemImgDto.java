package org.spring.backendspring.item.dto;

import java.time.LocalDateTime;

import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImgDto {

    // this is Img PK
    private Long id;
    // itemEntityId -> other Table
    private Long itemId;

    private String oldName;
    private String newName;
    
    private String fileUrl;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    

    // N:1
    @JsonIgnore
    private ItemEntity itemEntity;

    public static ItemImgDto toItemImgDto(ItemImgEntity itemImgEntity){

        return ItemImgDto.builder()
                .id(itemImgEntity.getId())
                .itemId(itemImgEntity.getItemEntity().getId())
                .newName(itemImgEntity.getNewName())
                .oldName(itemImgEntity.getOldName())
                .createTime(itemImgEntity.getCreateTime())
                .updateTime(itemImgEntity.getUpdateTime())
                .build();

    }
}
