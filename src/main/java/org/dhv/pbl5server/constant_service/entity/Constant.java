package org.dhv.pbl5server.constant_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dhv.pbl5server.common_service.model.AbstractEntity;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "constants")
public class Constant extends AbstractEntity {
    @Id
    private UUID constantId;
    @Column(nullable = false)
    private Integer constantType;
    private String constantName;
}
