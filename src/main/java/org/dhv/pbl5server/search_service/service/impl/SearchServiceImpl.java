package org.dhv.pbl5server.search_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhv.pbl5server.common_service.model.ApiDataResponse;
import org.dhv.pbl5server.common_service.utils.PageUtils;
import org.dhv.pbl5server.profile_service.entity.Company;
import org.dhv.pbl5server.profile_service.entity.User;
import org.dhv.pbl5server.profile_service.mapper.CompanyMapper;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.dhv.pbl5server.profile_service.repository.CompanyRepository;
import org.dhv.pbl5server.profile_service.repository.UserRepository;
import org.dhv.pbl5server.search_service.enums.SearchType;
import org.dhv.pbl5server.search_service.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final UserMapper userMapper;


    @Override
    public ApiDataResponse searchUsers(String query, SearchType type, Pageable request) {
        Page<User> page = switch (type) {
            case NAME -> userRepository.searchByName(query, request);
            case EMAIL -> userRepository.searchByEmail(query, request);
        };
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(userMapper::toUserProfileResponseWithBasicInfoOnly)
                .toList(),
            PageUtils.makePageInfo(page)
        );
    }

    @Override
    public ApiDataResponse searchCompanies(String query, SearchType type, Pageable request) {
        Page<Company> page = switch (type) {
            case NAME -> companyRepository.searchByName(query, request);
            case EMAIL -> companyRepository.searchByEmail(query, request);
        };
        return ApiDataResponse.success(page
                .getContent()
                .stream()
                .map(companyMapper::toCompanyResponseWithBasicInfoOnly)
                .toList(),
            PageUtils.makePageInfo(page)
        );
    }
}
