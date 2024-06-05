package com.iot.breathingMeachin.service.impl;

import com.iot.breathingMeachin.converter.ChiSoConverter;
import com.iot.breathingMeachin.dto.ChiSoDto;
import com.iot.breathingMeachin.entity.BenhNhanEntity;
import com.iot.breathingMeachin.entity.ChiSoEntity;
import com.iot.breathingMeachin.repository.BenhNhanRepository;
import com.iot.breathingMeachin.repository.ChiSoRepository;
import com.iot.breathingMeachin.service.IChiSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChiSoService implements IChiSoService {
    @Autowired
    private ChiSoRepository chiSoRepository;
    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private ChiSoConverter chiSoConverter;
    @Override
    public String saveChiSo(ChiSoDto chiSoDto) {
        try {
            ChiSoEntity entity = chiSoConverter.toEntity(chiSoDto);
            chiSoRepository.save(entity);
            return "Save ChiSo successfully!!";
        } catch (Exception e) {
            return "Save ChiSo failed!!";
        }
    }

    @Override
    public List<ChiSoDto> getChiSoByBenhNhan(Integer idBenhNhan) {
        List<ChiSoDto> chiSoDtoList = new ArrayList<>();
        try {
            List<ChiSoEntity> chiSoEntityList = chiSoRepository.getAllByIdBenhNhan(idBenhNhan);
            for (ChiSoEntity entity : chiSoEntityList) {
                chiSoDtoList.add(chiSoConverter.toDto(entity));
            }
            return chiSoDtoList;
        } catch (Exception e) {
            return chiSoDtoList;
        }
    }

    @Override
    public String saveListChiSo(List<ChiSoDto> chiSoDtoList) {
        try {
            List<ChiSoEntity> chiSoEntityList = new ArrayList<>();
            for (ChiSoDto dto : chiSoDtoList) {
                BenhNhanEntity benhNhanEntity =
                        benhNhanRepository.findById(dto.getIdBenhNhan()).orElse(new BenhNhanEntity());
                chiSoEntityList.add(chiSoConverter.toEntity(dto));
            }
            for (ChiSoEntity entity : chiSoEntityList) {
                chiSoRepository.save(entity);
            }
            return "Save list ChiSo successfully!!";
        } catch (Exception e) {
            return "Save list ChiSo failed!!";
        }
    }
}
