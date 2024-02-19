package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backoffice.operations.entity.ReportAnIssue;

public interface ReportIssueRepository extends JpaRepository<ReportAnIssue, String> {

}
