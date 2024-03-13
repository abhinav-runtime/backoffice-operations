package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.ProfileParameter;
@Repository
public interface ProfileParameterRepo extends JpaRepository<ProfileParameter, Long> {

}
