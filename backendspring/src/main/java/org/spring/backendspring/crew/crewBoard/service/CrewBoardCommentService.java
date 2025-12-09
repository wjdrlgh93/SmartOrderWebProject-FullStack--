package org.spring.backendspring.crew.crewBoard.service;


import org.spring.backendspring.common.dto.PagedResponse;
import org.spring.backendspring.crew.crewBoard.dto.CrewBoardCommentDto;

public interface CrewBoardCommentService {

    CrewBoardCommentDto createComment(CrewBoardCommentDto commentDto, Long boardId, Long loginUserId);

    PagedResponse<CrewBoardCommentDto> commentList(Long crewId, Long boardId, int page, int size);

    CrewBoardCommentDto commentDetail(Long boardId, Long id);

    CrewBoardCommentDto updateComment(Long id, CrewBoardCommentDto crewBoardCommentDto, Long loginUserId);

    void deleteComment(Long id, Long loginUserId);

}
