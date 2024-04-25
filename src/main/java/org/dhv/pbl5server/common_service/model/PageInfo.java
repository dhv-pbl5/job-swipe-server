package org.dhv.pbl5server.common_service.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

@Data
@NoArgsConstructor
@JsonSnakeCaseNaming
public class PageInfo {
    private int currentPage;
    private int nextPage;
    private int previousPage;
    private int totalPage;
    private long totalCount;

    @Builder
    public PageInfo(int currentPage, int totalPage, long totalCount) {
        this.currentPage = currentPage;
        this.nextPage = (currentPage >= totalPage) ? totalPage : currentPage + 1;
        this.previousPage = (currentPage <= 1)
            ? currentPage
            : (currentPage > totalPage)
            ? totalPage
            : currentPage - 1;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }
}
