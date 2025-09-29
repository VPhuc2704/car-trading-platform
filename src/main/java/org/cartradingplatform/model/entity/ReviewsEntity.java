package org.cartradingplatform.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewsEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UsersEntity reviewer;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "reviewed_id", nullable = false)
    private UsersEntity reviewed;

    @Column(name ="rating")
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT", length = 512)
    private String comment;

}
