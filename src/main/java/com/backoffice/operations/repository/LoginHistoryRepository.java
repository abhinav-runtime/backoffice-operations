package com.backoffice.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backoffice.operations.entity.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, String> {
	List<LoginHistory> findByUniqueKey(String uniqueKey);
	LoginHistory findFirstByOrderByLoginTimestampDesc();
}
