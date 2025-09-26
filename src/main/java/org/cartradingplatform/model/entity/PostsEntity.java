package org.cartradingplatform.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.cartradingplatform.model.enums.PostStatus;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsEntity extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "location", length = 100)
    private String location;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)  // Một seller có nhiều post
    @JoinColumn(name = "seller_id", nullable = false)
    private UsersEntity seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status = PostStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "car_detail_id", nullable = false)
    private CarDetailEntity carDetail;
}
