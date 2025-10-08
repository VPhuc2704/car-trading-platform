package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.CarDetailDTO;
import org.cartradingplatform.model.entity.CarDetailEntity;

public class CarDetailMapper {
    public static CarDetailDTO toDTO(CarDetailEntity carDetailEntity) {
        return  CarDetailDTO.builder()
                .make(carDetailEntity.getMake())
                .model(carDetailEntity.getModel())
                .year(carDetailEntity.getYear())
                .mileage(carDetailEntity.getMileage())
                .fuelType(carDetailEntity.getFuelType())
                .transmission(carDetailEntity.getTransmission())
                .color(carDetailEntity.getColor())
                .condition(carDetailEntity.getCondition())
                .build();
    }

    public static CarDetailEntity toEntity(CarDetailDTO dto) {
        if (dto == null) return null;
        return CarDetailEntity.builder()
                .make(dto.getMake())
                .model(dto.getModel())
                .year(dto.getYear())
                .mileage(dto.getMileage())
                .fuelType(dto.getFuelType())
                .transmission(dto.getTransmission())
                .color(dto.getColor())
                .condition(dto.getCondition())
                .build();
    }
}
