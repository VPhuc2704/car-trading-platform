package org.cartradingplatform.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.cartradingplatform.model.enums.PaymentMethod;
import org.cartradingplatform.model.enums.PaymentStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "post_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPaymentsEntity extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "payment_id")
        private Long paymentId;

        @ManyToOne
        @JoinColumn(name = "post_id", nullable = false)
        private PostsEntity post;

        @ManyToOne
        @JoinColumn(name = "seller_id", nullable = false)
        private UsersEntity seller;

        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_method", nullable = false)
        private PaymentMethod paymentMethod;

        @Enumerated(EnumType.STRING)
        private PaymentStatus status = PaymentStatus.PENDING;
}
