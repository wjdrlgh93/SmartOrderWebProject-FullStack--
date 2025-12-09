package org.spring.backendspring.item.service;

import java.io.IOException;

import org.spring.backendspring.item.dto.ItemReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemReplyService {

    Long insertReply(ItemReplyDto itemReplyDto);

    Page<ItemReplyDto> getReplyPage(Long itemId, Pageable pageable);
    // U
    void update(ItemReplyDto itemReplyDto) throws IOException;
    // D
    void deleteReply(Long replyId, Long memberId);
}
