package org.cartradingplatform.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String title;
    private String description;
    private BigDecimal price;
    private String status;
    private String location;
    private List<String> images = new ArrayList<>();;
    private CarDetailDTO carDetailDTO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
