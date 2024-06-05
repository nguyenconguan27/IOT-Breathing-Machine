package com.iot.breathingMeachin.converter;

import com.iot.breathingMeachin.dto.MayThoDto;
import com.iot.breathingMeachin.entity.MayThoEntity;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MayThoConverter {
    public MayThoDto toDto(MayThoEntity entity) {

        try {
            ModelMapper modelMapper = new ModelMapper();
            MayThoDto dto = modelMapper.map(entity, MayThoDto.class);
            return dto;
        } catch (Exception e) {
            System.out.println("Convert from MayThoEntity to MayThoDto error");
            e.printStackTrace();
            return new MayThoDto();
        }
    }

    public MayThoEntity toEntity(MayThoDto dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            MayThoEntity entity = modelMapper.map(dto, MayThoEntity.class);
            return entity;
        } catch (Exception e) {
            System.out.println("Convert from MayThoDto to MayThoEntity error");
            e.printStackTrace();
            return new MayThoEntity();
        }
    }
}
