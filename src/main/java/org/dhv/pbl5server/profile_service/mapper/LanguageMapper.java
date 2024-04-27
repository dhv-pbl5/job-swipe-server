package org.dhv.pbl5server.profile_service.mapper;

import org.dhv.pbl5server.common_service.config.SpringMapStructConfig;
import org.dhv.pbl5server.constant_service.mapper.ConstantMapper;
import org.dhv.pbl5server.profile_service.entity.Language;
import org.dhv.pbl5server.profile_service.payload.request.LanguageRequest;
import org.dhv.pbl5server.profile_service.payload.response.LanguageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = SpringMapStructConfig.class, uses = {ConstantMapper.class})
public interface LanguageMapper {
    public static final String NAMED_ToLanguageResponse = "toLanguageResponse";

    @Mapping(source = "score", target = "languageScore")
    @Mapping(source = "certificateDate", target = "languageCertificateDate")
    Language toLanguage(LanguageRequest request);

    @Mapping(source = "request.id", target = "id")
    @Mapping(source = "request.language", target = "language")
    @Mapping(source = "request.score", target = "languageScore")
    @Mapping(source = "request.certificateDate", target = "languageCertificateDate")
    Language toLanguage(Language language, LanguageRequest request);

    @Named(NAMED_ToLanguageResponse)
    @Mapping(source = "languageScore", target = "score")
    @Mapping(source = "languageCertificateDate", target = "certificateDate")
    LanguageResponse toLanguageResponse(Language language);
}
