package org.dhv.pbl5server.search_service.service;

import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.search_service.enums.SearchType;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    ApiDataResponse searchUsers(String query, SearchType type, Pageable request);

    ApiDataResponse searchCompanies(String query, SearchType type, Pageable request);
}
