package org.dhv.pbl5server.notification_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.dhv.pbl5server.authentication_service.entity.Account;
import org.dhv.pbl5server.common_service.model.AbstractEntity;
import org.dhv.pbl5server.constant_service.entity.Constant;

import java.util.UUID;

// git commit -m "PBL-597 realtime conversation"

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends AbstractEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID objectId;
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type")
    private Constant type;
    private boolean readStatus = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private Account receiver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private Account sender;
}
