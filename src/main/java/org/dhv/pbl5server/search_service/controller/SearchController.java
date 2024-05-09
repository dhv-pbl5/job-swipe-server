package org.dhv.pbl5server.search_service.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.dhv.pbl5server.authentication_service.annotation.PreAuthorizeSystemRole;
import org.dhv.pbl5server.common_service.enums.AbstractEnum;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.search_service.enums.SearchType;
import org.dhv.pbl5server.search_service.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @PreAuthorizeSystemRole
    @GetMapping("/users")
    public ResponseEntity<ApiDataResponse> searchUsers(
        @RequestParam("query") String query,
        @RequestParam("type") String type,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @Nullable @RequestParam("sort_by") String sortBy,
        @Nullable @RequestParam("order") String order
    ) {
        var searchType = AbstractEnum.fromString(SearchType.values(), type);
        var pageRequest = PageUtils.makePageRequest(sortBy, order, page, paging);
        return ResponseEntity.ok(service.searchUsers(query, searchType, pageRequest));
    }

    @PreAuthorizeSystemRole
    @GetMapping("/companies")
    public ResponseEntity<ApiDataResponse> searchCompanies(
        @RequestParam("query") String query,
        @RequestParam("type") String type,
        @Nullable @RequestParam("page") Integer page,
        @Nullable @RequestParam("paging") Integer paging,
        @Nullable @RequestParam("sort_by") String sortBy,
        @Nullable @RequestParam("order") String order
    ) {
        var searchType = AbstractEnum.fromString(SearchType.values(), type);
        var pageRequest = PageUtils.makePageRequest(sortBy, order, page, paging);
        return ResponseEntity.ok(service.searchCompanies(query, searchType, pageRequest));
    }
}
