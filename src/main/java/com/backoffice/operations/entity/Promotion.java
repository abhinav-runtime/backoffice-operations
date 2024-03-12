package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_promotions_bk")
public class Promotion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(nullable = false)
    private String imageUrl;

    private String urlType;

    private String url;

    private int priority;

    private String text1;

    private String text2;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime lastUpdatedAt;

    private String updatedBy;
}
