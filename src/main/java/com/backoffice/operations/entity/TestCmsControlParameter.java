package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Table(name = "az_test_cms_controller_parameter_bk")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCmsControlParameter {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    String id;

    @Column(nullable = false, unique = true)
    private String variable;

    @Column(nullable = false)
    private String value;


}
