package com.iot.breathingMeachin.controller;

import com.iot.breathingMeachin.dto.ChiSoDto;
import com.iot.breathingMeachin.service.IChiSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/iot")
public class ChiSoController {
    @Autowired
    private IChiSoService chiSoService;
    @PostMapping("/chiSo/save")
    public String saveChiSo(@RequestBody ChiSoDto chiSoDto) {
        String mess = chiSoService.saveChiSo(chiSoDto);
        return mess;
    }
    @PostMapping("/chiSo/save/list")
    public String saveListChiSo(@RequestBody List<ChiSoDto> chiSoDtoList){
        String mess = chiSoService.saveListChiSo(chiSoDtoList);
        return mess;
    }
        @GetMapping("/chiSo/benhNhan")
    public List<ChiSoDto> getChiSoByBenhNhan(@RequestParam("idBenhNhan") Integer idBenhNhan) {
        List<ChiSoDto> chiSoDtoList = chiSoService.getChiSoByBenhNhan(idBenhNhan);
        return chiSoDtoList;
    }
}
