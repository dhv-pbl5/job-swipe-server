package org.dhv.pbl5server.common_service.utils;

import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.enums.DataSortOrder;
import org.dhv.pbl5server.common_service.model.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils {


    /**
     * Handle make page request for query
     *
     * @param sortBy Sort By Field
     * @param order  Order By Desc Or Asc
     * @param page   Page No
     * @param paging Page Size
     * @return Pageable
     */
    public static Pageable makePageRequest(String sortBy, String order, Integer page, Integer paging) {
        page = (page == null || page <= 0) ? 1 : page;
        paging = paging == null ? 15 : (paging >= 30) ? 30 : paging;
        Sort sort = null;
        if (CommonUtils.isNotEmptyOrNullString(order) && CommonUtils.isNotEmptyOrNullString(sortBy)) {
            String sortField = CommonUtils.convertToCamelCase(sortBy);
            DataSortOrder dataSortOrder = AbstractEnum.fromString(DataSortOrder.values(), order);
            sort = switch (dataSortOrder) {
                case DESC -> Sort.by(Sort.Direction.DESC, sortField);
                case ASC -> Sort.by(Sort.Direction.ASC, sortField);
            };
        }
        return sort != null ? PageRequest.of(page - 1, paging, sort) : PageRequest.of(page - 1, paging);
    }

    /**
     * Page info utils
     */
    public static PageInfo makePageInfo(int currentPage, int paging, long totalCount) {
        return PageInfo.builder()
            .currentPage(currentPage)
            .totalPage((int) Math.ceil(totalCount % paging))
            .totalCount(totalCount)
            .build();
    }

    public static PageInfo makePageInfo(Page<?> page) {
        if (page.getTotalPages() != 0)
            return PageInfo.builder()
                .currentPage(page.getNumber() + 1)
                .totalPage(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .build();
        return makePageInfo(page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }

    private PageUtils() {
    }
}
