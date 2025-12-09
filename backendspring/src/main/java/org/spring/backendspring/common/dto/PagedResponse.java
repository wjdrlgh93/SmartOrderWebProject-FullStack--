package org.spring.backendspring.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PagedResponse<T> {

    private List<T> content; // 현재 페이지의 데이터 목록

    private final int currentPage; // 현재 페이지 (1부터 표시용)
    private final int totalPages; // 전체 페이지 수
    private final long totalElements; // 전체 데이터 개수

    private final boolean hasNext; // 다음 페이지 존재 여부
    private final boolean hasPrevious; // 이전 페이지 존재 여부
    private final boolean hasFirst; // "처음으로" 버튼 활성 여부
    private final boolean hasLast; // "마지막으로" 버튼 활성 여부

    private final int startPage; // 현재 블록의 시작 페이지 번호
    private final int endPage; // 현재 블록의 끝 페이지 번호
    private final int blockSize; // 블록 크기 (보통 5)

    @Builder
    public PagedResponse(List<T> content, int currentPage, int totalPages, long totalElements,
            boolean hasNext, boolean hasPrevious, boolean hasFirst, boolean hasLast,
            int startPage, int endPage, int blockSize) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.hasFirst = hasFirst;
        this.hasLast = hasLast;
        this.startPage = startPage;
        this.endPage = endPage;
        this.blockSize = blockSize;
    }

    // ✅ Page<T>만 넘기면 바로 페이징 정보 채우는 생성자
    public static <T> PagedResponse<T> of(Page<T> page) {
        int blockSize = 5;
        int currentPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        return PagedResponse.<T>builder()
                .content(page.getContent())
                .currentPage(currentPage)
                .totalPages(totalPages)
                .totalElements(page.getTotalElements())
                .hasPrevious(page.hasPrevious())
                .hasNext(page.hasNext())
                .hasFirst(currentPage > 1)
                .hasLast(currentPage < totalPages)
                .startPage(startPage)
                .endPage(endPage)
                .blockSize(blockSize)
                .build();
    }
}
