package com.medilabo.risk.controller;

import com.medilabo.risk.model.RiskLevel;
import com.medilabo.risk.service.RiskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<Map<String, String>> getRisk(
            @PathVariable Long patientId,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        RiskLevel level = riskService.assess(patientId, authHeader);
        return ResponseEntity.ok(Map.of("riskLevel", level.name()));
    }
}
