package org.spring.backendspring.item.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.spring.backendspring.board.dto.BoardImgDto;
import org.spring.backendspring.cart.entity.CartItemEntity;
import org.spring.backendspring.item.entity.ItemEntity;
import org.spring.backendspring.item.entity.ItemImgEntity;
import org.spring.backendspring.item.entity.ItemReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

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
public class ItemDto {


    private Long id;
    
    private String itemDetail;
    private String itemTitle;
    private int itemPrice;
    private String category;


    private int itemSize;
    private int attachFile;

   
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Long memberId;

    private MemberEntity memberEntity;

    private MultipartFile itemFile;
    private String newFileName;
    private String oldFileName;
    private List<MultipartFile> itemFileList;
    private List<ItemImgDto> itemImgDtos;

    private String fileUrl;
    

    private List<CartItemEntity> itemListEntities;
    private List<ItemReplyEntity> itemReplyEntities;

    // toDTO
    public static ItemDto toItemDto ( ItemEntity itemEntity){

        String newFileName = null;
        String oldFileName = null;

         List<ItemImgDto> itemImgDtos = null;

        if(itemEntity.getAttachFile() != 0 &&
            itemEntity.getItemImgEntities() != null &&
            !itemEntity.getItemImgEntities().isEmpty()){
                newFileName = itemEntity.getItemImgEntities().get(0).getNewName();
                oldFileName = itemEntity.getItemImgEntities().get(0).getOldName();

                itemImgDtos = itemEntity.getItemImgEntities().stream()
                                .map(ItemImgDto::toItemImgDto)
                                .collect(Collectors.toList());
            }
            return ItemDto.builder()
                        .id(itemEntity.getId())
                        .itemTitle(itemEntity.getItemTitle())
                        .itemDetail(itemEntity.getItemDetail())
                        .itemPrice(itemEntity.getItemPrice())
                        .itemSize(itemEntity.getItemSize())
                        .category(itemEntity.getCategory())
                        .attachFile(itemEntity.getAttachFile())
                        .memberId(itemEntity.getMemberEntity().getId())
                        .itemImgDtos(itemImgDtos)
                        .newFileName(newFileName)
                        .oldFileName(oldFileName)
                        .createTime(itemEntity.getCreateTime())
                        .updateTime(itemEntity.getUpdateTime())
                        .build();

    }

}
