package org.dhv.pbl5server.constant_service.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "constants")
public class Constant extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID constantId;
    @Column(nullable = false)
    private String constantType;
    private String constantName;
    @Type(JsonBinaryType.class)
    private Object note;
}
