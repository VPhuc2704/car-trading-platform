package org.cartradingplatform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDetailDTO {
    private String make;           // Hãng xe
    private String model;          // Dòng xe
    private Integer year;          // Năm sản xuất
    private Integer mileage;       // Số km đã đi
    private String fuelType;       // Nhiên liệu
    private String transmission;   // Hộp số
    private String color;          // Màu xe
    private String condition;      // Tình trạng: Mới/Cũ
}
