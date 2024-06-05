package com.iot.breathingMeachin.service;

import com.iot.breathingMeachin.dto.ChiSoDto;

import java.util.List;

public interface IChiSoService {
    String saveChiSo(ChiSoDto chiSoDto);
    List<ChiSoDto> getChiSoByBenhNhan(Integer idBenhNhan);
    String saveListChiSo(List<ChiSoDto> chiSoDtoList);
}
