package org.spring.backendspring.item.entity;

import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.item.dto.ItemImgDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "item_image_tb" )
public class ItemImgEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    @Column(nullable = false)
    private String oldName;
    @Column(nullable = false)
    private String newName;

    // N:1
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity itemEntity;

    public static ItemImgEntity toItemImgEntity(ItemImgDto imgDto){
        return ItemImgEntity.builder()
                .oldName(imgDto.getOldName())
                .newName(imgDto.getNewName())
                .itemEntity(imgDto.getItemEntity())
            .build();
    }

   
}
