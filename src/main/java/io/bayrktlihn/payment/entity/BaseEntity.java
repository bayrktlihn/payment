package io.bayrktlihn.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class BaseEntity {

    @Builder.Default
    @Version
    @Column(columnDefinition = "bigint not null default 0")
    private Long version = 0L;

    @Builder.Default
    @Column(columnDefinition = "boolean not null default true")
    private boolean active = true;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime passiveDate;


    public void makePassive() {
        active = false;
        passiveDate = LocalDateTime.now();
    }


    @PrePersist
    void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
