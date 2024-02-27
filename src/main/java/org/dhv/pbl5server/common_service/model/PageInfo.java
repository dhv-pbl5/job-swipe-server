package org.dhv.pbl5server.common_service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageInfo {
    private int currentPage;
    private int nextPage;
    private int prevPage;
    private int totalPage;
    private long totalCount;

    public PageInfo(int currentPage, int totalPage, long totalCount) {
        this.currentPage = currentPage;
        this.nextPage = (currentPage >= totalPage) ? totalPage : currentPage + 1;
        this.prevPage = (currentPage <= 1) ? currentPage : currentPage - 1;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }

    public static PageInfo createPageInfoWithPaging(Integer currentPage, Integer paging, Long totalCount) {
        return new PageInfo(currentPage, Math.ceilMod(totalCount, paging), totalCount);
    }
}
