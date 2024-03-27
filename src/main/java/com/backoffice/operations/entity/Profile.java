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
@Table(name = "az_profile_bk")
public class Profile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    private String uniqueKeyCivilId;
    private String nId;
    private String emailId;
    private String fullName;
    private String mobNum;
    private int mobisdno;
    private boolean emailStatementFlag;
    private String civilId;
    private String expiryDate;
    private String userId;
    private int mobileChangeAttemptLimit;
    private LocalDateTime mobileChangeLockoutDuration;
    private LocalDate mobileNoChangedLockoutDaysDuration;
    private int emailIdChangeAttemptLimit;
    private int emailIdVerificationLinkResendAttempts;
    private LocalDateTime emailIdVerificationLinkTimeout;
    private LocalDateTime emailIdChangeLockoutDuration;
    private LocalDate emailIdChangedLockoutDaysDuration;
    private int emailStatementEnableDisable;



}
