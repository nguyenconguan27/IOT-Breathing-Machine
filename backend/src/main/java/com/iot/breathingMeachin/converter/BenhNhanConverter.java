package com.iot.breathingMeachin.converter;

import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.entity.BenhNhanEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BenhNhanConverter {
    public BenhNhanDto toDto (BenhNhanEntity entity) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            BenhNhanDto dto = modelMapper.map(entity, BenhNhanDto.class);
            return dto;
        } catch (Exception e) {
            System.out.println("Convert from BenhNhanEntity to BenhNhanDto error");
            e.printStackTrace();
            return new BenhNhanDto();
        }
    }

    public BenhNhanEntity toEntity (BenhNhanDto dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            BenhNhanEntity entity = modelMapper.map(dto, BenhNhanEntity.class);
            return entity;
        } catch (Exception e) {
            System.out.println("Convert from BenhNhanDto to BenhNhanEntity error");
            e.printStackTrace();
            return new BenhNhanEntity();
        }
    }
}
