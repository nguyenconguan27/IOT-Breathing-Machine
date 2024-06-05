package com.iot.breathingMeachin.service.impl;

import com.iot.breathingMeachin.converter.BenhNhanConverter;
import com.iot.breathingMeachin.converter.MayThoConverter;
import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.dto.MayThoDto;
import com.iot.breathingMeachin.entity.MayThoEntity;
import com.iot.breathingMeachin.repository.MayThoRepository;
import com.iot.breathingMeachin.service.IMayThoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MayThoService implements IMayThoService {
    @Autowired
    private MayThoRepository mayThoRepository;
    @Autowired
    private MayThoConverter mayThoConverter;
    @Autowired
    private BenhNhanConverter benhNhanConverter;
    @Override
    public String addMayTho(MayThoDto mayThoDto) {
        try {
            MayThoEntity entity = mayThoConverter.toEntity(mayThoDto);
            mayThoRepository.save(entity);
            return "Save MayTho successfully!!";
        } catch (Exception e){
            return "Save MayTho failed!!";
        }
    }

    @Override
    public String updateMayTho(Integer idMayTho, BenhNhanDto benhNhanDto) {
        try {
            MayThoEntity mayThoEntity = mayThoRepository.findById(idMayTho).orElse(new MayThoEntity());
            mayThoEntity.setBenhNhanEntity(benhNhanConverter.toEntity(benhNhanDto));
            mayThoRepository.save(mayThoEntity);
            return "Ket noi may tho voi benh nhan thanh cong!!";
        } catch (Exception e) {
            return "Ket noi benh nhan voi may tho that bai!!";
        }
    }

    @Override
    public String updateMayTho(Integer idMayTho) {
        try {
            mayThoRepository.updateById(idMayTho);
            return "cap nhap thanh cong";
        }
        catch (Exception e) {
            return "cap nhap that bai";
        }

    }

}
