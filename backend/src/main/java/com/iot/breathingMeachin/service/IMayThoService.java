package com.iot.breathingMeachin.service;

import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.dto.MayThoDto;

public interface IMayThoService {
    String addMayTho(MayThoDto mayThoDto);
    String updateMayTho(Integer idMayTho, BenhNhanDto benhNhanDto);

    String updateMayTho(Integer idMayTho);
}
