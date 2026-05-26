package com.medilabo.risk.controller;

import com.medilabo.risk.model.RiskLevel;
import com.medilabo.risk.service.RiskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller class for handling risk assessment related endpoints
 * Provides functionality to assess the risk level of a patient based on their ID and authentication token
 */
@Slf4j
@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    /**
     * Endpoint to assess the risk level of a patient by their ID
     * Requires an Authorization header with a valid JWT token
     * @param patientId
     * @param request
     * @return
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<Map<String, String>> getRisk(
            @PathVariable Long patientId,
            HttpServletRequest request) {
        log.info("GET /api/risk/{} called", patientId);

        String authHeader = request.getHeader("Authorization");
        try {
            RiskLevel level = riskService.assess(patientId, authHeader);
            log.info("Risk assessment for patientId={} resulted in: {}", patientId, level);
            return ResponseEntity.ok(Map.of("riskLevel", level.name()));
        } catch (Exception e) {
            log.error("Error assessing risk for patientId={}: {}", patientId, e.getMessage(), e);
            throw e;
        }
    }
}
