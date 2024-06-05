package com.iot.breathingMeachin.controller;

import com.iot.breathingMeachin.dto.BenhNhanDto;
import com.iot.breathingMeachin.service.IBenhNhanService;
import com.iot.breathingMeachin.service.IMayThoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/iot")
public class BenhNhanController {
    @Autowired
    private IBenhNhanService benhNhanService;
    @Autowired
    private IMayThoService mayThoService;
    @PostMapping("/benhNhan/import")
    public BenhNhanDto addBenhNhan(@RequestBody BenhNhanDto benhNhanDto) {
        BenhNhanDto benhNhan = benhNhanService.addBenhNhan(benhNhanDto);
        return benhNhan;
    }

    @GetMapping("/benhNhan")
    public BenhNhanDto getBenhNhanTheoId(@RequestParam("idBenhNhan") Integer idBenhNhan) {
        BenhNhanDto benhNhan = benhNhanService.getOneBenhNhan(idBenhNhan);
        return benhNhan;
    }


    @DeleteMapping("/benhNhan/delete")
    public String deleteBenhNhan(@RequestParam("id") Integer idBenhNhan, @RequestParam("idMayTho") Integer idMayTho) {
        mayThoService.updateMayTho(idMayTho);
        String mess = benhNhanService.deleteBenhNhan(idBenhNhan);
        return mess;
    }
}
