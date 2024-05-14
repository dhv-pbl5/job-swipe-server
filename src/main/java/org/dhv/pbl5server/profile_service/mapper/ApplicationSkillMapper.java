package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.ApplicationSkill;
import org.dhv.pbl5server.profile_service.payload.request.ApplicationSkillRequest;
import org.dhv.pbl5server.profile_service.payload.response.ApplicationSkillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// git commit -m "PBL-536 user profile"

@Mapper(config = SpringMapStructConfig.class, uses = { ConstantMapper.class })
public interface ApplicationSkillMapper {
    @Mapping(source = "skill", target = "skill")
    ApplicationSkill toApplicationSkill(ApplicationSkillRequest request);

    @Mapping(source = "request.id", target = "id")
    @Mapping(source = "request.skill", target = "skill")
    @Mapping(source = "request.note", target = "note")
    ApplicationSkill toApplicationSkill(ApplicationSkill applicationSkill, ApplicationSkillRequest request);

    @Mapping(source = "skill", target = "skill")
    ApplicationSkillResponse toApplicationSkillResponse(ApplicationSkill applicationSkill);
}
