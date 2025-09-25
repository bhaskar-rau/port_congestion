package com.esm.dynamicpricing.controller;


import com.esm.dynamicpricing.repository.CamPortRepository;
import com.esm.dynamicpricing.repository.CamTerminalDepotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortController {

    private final CamPortRepository camPortRepository;
    private final CamTerminalDepotRepository camTerminalDepotRepository;

    @GetMapping
    public List<String> getAllPortCodes() {
        return camPortRepository.findAllPortCodes();
    }

    @GetMapping("/{portCode}/terminals")
    public List<String> getTerminalsByPortCode(@PathVariable String portCode) {
        return camTerminalDepotRepository.findTerminalsByPortCode(portCode);
    }
}
