package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Table(name = "az_source_operation_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  SourceOperation {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(nullable = false, unique = true)
    private String sourceCode;
    @Column(nullable = false, unique = true)
    private String sourceOperation;
    private String sourceSystem;

}
