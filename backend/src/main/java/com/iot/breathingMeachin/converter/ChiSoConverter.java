package com.iot.breathingMeachin.converter;

import com.iot.breathingMeachin.dto.ChiSoDto;
import com.iot.breathingMeachin.entity.BenhNhanEntity;
import com.iot.breathingMeachin.entity.ChiSoEntity;
import com.iot.breathingMeachin.repository.BenhNhanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChiSoConverter {
    @Autowired
    private BenhNhanRepository benhNhanRepository;
    public ChiSoDto toDto(ChiSoEntity entity) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            ChiSoDto dto = modelMapper.map(entity, ChiSoDto.class);
            dto.setIdBenhNhan(entity.getBenhNhanEntity().getId());
            return dto;
        } catch (Exception e) {
            System.out.println("Convert from ChiSoEntity to ChiSoDto error");
            e.printStackTrace();
            return new ChiSoDto();
        }
    }

    public ChiSoEntity toEntity(ChiSoDto dto) {

        try {
            ModelMapper modelMapper = new ModelMapper();
            ChiSoEntity entity = modelMapper.map(dto, ChiSoEntity.class);
            Optional<BenhNhanEntity> benhNhanEntity = benhNhanRepository.findById(dto.getIdBenhNhan());
            entity.setBenhNhanEntity(benhNhanEntity.orElse(new BenhNhanEntity()));
            return entity;
        } catch (Exception e) {
            System.out.println("Convert from ChiSoDto to ChiSoEntity error");
            e.printStackTrace();
            return new ChiSoEntity();
        }
    }
}
