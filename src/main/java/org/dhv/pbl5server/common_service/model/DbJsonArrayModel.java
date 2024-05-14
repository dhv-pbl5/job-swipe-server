// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.annotation.JsonSnakeCaseNaming;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonSnakeCaseNaming
public abstract class DbJsonArrayModel<K> {
    protected K id;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    public abstract K generateId();
}
