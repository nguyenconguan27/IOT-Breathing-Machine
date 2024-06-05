package com.iot.breathingMeachin.service.impl;

import com.iot.breathingMeachin.converter.BenhNhanConverter;
import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.entity.BenhNhanEntity;
import com.iot.breathingMeachin.repository.BenhNhanRepository;
import com.iot.breathingMeachin.service.IBenhNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BenhNhanService implements IBenhNhanService {
    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private BenhNhanConverter benhNhanConverter;
    @Override
    public BenhNhanDto addBenhNhan(BenhNhanDto benhNhanDto) {
        try {
            BenhNhanEntity benhNhanEntity = benhNhanConverter.toEntity(benhNhanDto);
            benhNhanEntity = benhNhanRepository.save(benhNhanEntity);
            return benhNhanConverter.toDto(benhNhanEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String deleteBenhNhan(Integer idBenhNhan) {
        try {
            benhNhanRepository.deleteById(idBenhNhan);
            return "Delete benhNhan successfully!!";
        } catch (Exception e) {
            return "Delete benhNhan failed!!";
        }
    }

    @Override
    public BenhNhanDto getOneBenhNhan(Integer idBenhNhan) {
        try {
            BenhNhanEntity entity = benhNhanRepository.findById(idBenhNhan).orElse(new BenhNhanEntity());
            return benhNhanConverter.toDto(entity);
        } catch (Exception e) {
            return new BenhNhanDto();
        }
    }

    @Override
    public List<BenhNhanDto> getAllBenhNhan() {
        try {
            List<BenhNhanEntity> benhNhanEntityList = benhNhanRepository.findAll();
            List<BenhNhanDto> benhNhanDtoList = new ArrayList<>();
            for (BenhNhanEntity entity : benhNhanEntityList) {
                benhNhanDtoList.add(benhNhanConverter.toDto(entity));
            }
            return benhNhanDtoList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
