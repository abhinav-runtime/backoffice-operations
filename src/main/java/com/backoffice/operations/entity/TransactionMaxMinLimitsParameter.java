package com.backoffice.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "az_transaction_max_min_limits_parameter_bk")
public class TransactionMaxMinLimitsParameter {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
	
	@Column(name = "merchantOutletMaxLimits")
    private double merchantOutletMaxLimits;
    
    @Column(name = "onlineShoppingMaxLimits")
    private double onlineShoppingMaxLimits;
    
    @Column(name = "atmWithdrawalMaxLimits")
    private double atmWithdrawalMaxLimits;
    
    @Column(name = "tapAndPayMaxLimits")
    private double tapAndPayMaxLimits;
    
    @Column(name = "merchantOutletMinLimits")
    private double merchantOutletMinLimits;
    
    @Column(name = "onlineShoppingMinLimits")
    private double onlineShoppingMinLimits;
    
    @Column(name = "atmWithdrawalMinLimits")
    private double atmWithdrawalMinLimits;
    
    @Column(name = "tapAndPayMinLimits")
    private double tapAndPayMinLimits;
    
    // Getter and Setter
}
