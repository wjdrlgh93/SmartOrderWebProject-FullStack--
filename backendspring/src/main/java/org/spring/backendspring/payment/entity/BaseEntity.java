package org.spring.backendspring.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(value = AbstractMethodError.class)
public class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime CreateTime;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime UpdateTime;
}