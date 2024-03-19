package org.dhv.pbl5server.profile_service.model;

import org.dhv.pbl5server.common_service.converter.PostgresqlJsonArrayConverter;

public class OtherDescriptionConverter extends PostgresqlJsonArrayConverter<OtherDescription> {
    OtherDescriptionConverter() {
        super(OtherDescription.class);
    }
}
