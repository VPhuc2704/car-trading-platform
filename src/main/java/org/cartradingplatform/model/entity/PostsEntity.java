package org.cartradingplatform.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.model.enums.SellerType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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

    @Column(name = "phone_conact")
    private String phoneContact;

    @Enumerated(EnumType.STRING)
    @Column(name = "seller_type")
    private SellerType sellerType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // Một seller có nhiều post
    @JoinColumn(name = "seller_id", nullable = false)
    private UsersEntity seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status = PostStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "car_detail_id", nullable = false)
    private CarDetailEntity carDetail;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FavoriteEntity> favorites;

}
