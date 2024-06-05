package com.iot.breathingMeachin.controller;

import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.dto.MayThoDto;
import com.iot.breathingMeachin.service.impl.MayThoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/iot")
public class MayThoController {
    @Autowired
    private MayThoService mayThoService;

    // Thêm máy thở, không cần truyền gì (chỉ có 1 thuộc tính id tự tăng)
    @PostMapping("/mayTho/add")
    public String addMayTho(@RequestBody MayThoDto mayThoDto) {
        String mess = mayThoService.addMayTho(mayThoDto);
        return mess;
    }

    // ghép bệnh nhân vào máy thở
    @PutMapping("/mayTho/ketNoi")
    public String ketNoiMayTho(@RequestParam("idMayTho") Integer idMayTho, @RequestBody BenhNhanDto benhNhanDto) {
        String mess = mayThoService.updateMayTho(idMayTho, benhNhanDto);
        return mess;
    }
}
