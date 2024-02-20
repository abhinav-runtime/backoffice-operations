package com.backoffice.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "az_dashboard_bk")
public class DashboardEntity {

    @Id
    private String id;
    private String accountNumber;
    private String currency;
    private Double availableBalance;
}
