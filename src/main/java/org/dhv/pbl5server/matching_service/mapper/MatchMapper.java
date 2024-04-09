package org.dhv.pbl5server.matching_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.matching_service.entity.Match;
import org.dhv.pbl5server.matching_service.payload.MatchResponse;
import org.dhv.pbl5server.profile_service.mapper.CompanyMapper;
import org.dhv.pbl5server.profile_service.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = SpringMapStructConfig.class,
    uses = {
        UserMapper.class,
        CompanyMapper.class
    }
)
public interface MatchMapper {
    @Mapping(source = "user", target = "user", qualifiedByName = UserMapper.NAMED_ToUserProfileResponseWithBasicInfoOnly)
    @Mapping(source = "company", target = "company", qualifiedByName = CompanyMapper.NAMED_ToCompanyResponseWithBasicInfoOnly)
    MatchResponse toMatchResponse(Match match);
}
