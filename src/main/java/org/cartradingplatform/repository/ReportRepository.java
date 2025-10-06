package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.ReportEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByReporter(UsersEntity reporter);
    List<ReportEntity> findByReportedUser(UsersEntity reportedUser);
    List<ReportEntity> findByStatus(ReportStatus status);
}
