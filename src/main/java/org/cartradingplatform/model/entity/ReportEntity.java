package org.cartradingplatform.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.cartradingplatform.model.enums.ReportStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UsersEntity reporter; // người gửi báo cáo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private UsersEntity reportedUser; // người bị báo cáo

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    private LocalDateTime handledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by")
    private UsersEntity handledBy; // admin xử lý
}
