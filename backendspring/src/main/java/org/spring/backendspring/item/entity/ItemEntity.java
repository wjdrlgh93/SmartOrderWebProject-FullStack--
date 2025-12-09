package org.spring.backendspring.item.entity;

import java.util.List;

import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.common.BasicTime;
import org.spring.backendspring.member.entity.MemberEntity;
import org.spring.backendspring.item.dto.ItemDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "item_tb")
public class ItemEntity extends BasicTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private String itemDetail;
    @Column(nullable = false, unique = true)
    private String itemTitle;
    @Column(nullable = false)
    private int itemPrice;
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int itemSize;
    private int attachFile;

    // N:1 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    // 1:N
    @OneToMany(mappedBy = "itemEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<CartItemEntity> itemListEntities;

    @OneToMany(mappedBy = "itemEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<ItemImgEntity> itemImgEntities;


    @OneToMany(mappedBy = "itemEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<ItemReplyEntity> itemReplyEntities;


    // toEntity 
    public static ItemEntity toItemEntity(ItemDto itemDto) {
        //builder()
        int attachFileValue = (itemDto.getNewFileName() != null &&
                !itemDto.getNewFileName().isEmpty()) ? 1 : 0;

        return ItemEntity.builder()
                    .id(itemDto.getId())
                    .itemTitle(itemDto.getItemTitle())
                    .itemDetail(itemDto.getItemDetail())
                    .itemPrice(itemDto.getItemPrice())
                    .itemSize(itemDto.getItemSize())
                    .category(itemDto.getCategory())
                    .memberEntity(itemDto.getMemberEntity())
                    .attachFile(attachFileValue)
        .build();

    }

}
