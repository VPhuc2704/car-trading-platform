package org.cartradingplatform.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;



@Entity
@Table(name = "car_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "make", length = 100, nullable = false)
    private String make;            // Hãng xe

    @Column(name = "model", length = 100, nullable = false)
    private String model;           // Dòng xe

    @Column(name = "manufacture_year", nullable = false)
    private Integer year;           // Năm sản xuất

    @Column(name = "mileage")
    private Integer mileage;        // Số km đã đi

    @Column(name = "fuel_type", length = 50)
    private String fuelType;        // Nhiên liệu

    @Column(name = "transmission", length = 50)
    private String transmission;    // Hộp số

    @Column(name = "color", length = 50)
    private String color;           // Màu xe

    @Column(name = "car_condition", length = 20)
    private String condition;       // Tình trạng: Mới/Cũ

    @OneToOne(mappedBy = "carDetail")
    private PostsEntity post;
}
