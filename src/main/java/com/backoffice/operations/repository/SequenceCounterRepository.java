package com.backoffice.operations.repository;

import com.backoffice.operations.entity.SequenceCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceCounterRepository extends JpaRepository<SequenceCounter, Long> {
}
