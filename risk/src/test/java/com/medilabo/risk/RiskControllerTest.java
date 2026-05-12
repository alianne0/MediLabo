package com.medilabo.risk;

import com.medilabo.risk.controller.RiskController;
import com.medilabo.risk.model.RiskLevel;
import com.medilabo.risk.security.JwtRequestFilter;
import com.medilabo.risk.service.RiskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RiskController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class RiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RiskService riskService;

    @Test
    void getRisk_shouldReturnNone() throws Exception {
        when(riskService.assess(anyLong(), anyString())).thenReturn(RiskLevel.NONE);

        mockMvc.perform(get("/api/risk/1")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("NONE"));
    }

    @Test
    void getRisk_shouldReturnBorderline() throws Exception {
        when(riskService.assess(anyLong(), anyString())).thenReturn(RiskLevel.BORDERLINE);

        mockMvc.perform(get("/api/risk/2")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("BORDERLINE"));
    }

    @Test
    void getRisk_shouldReturnInDanger() throws Exception {
        when(riskService.assess(anyLong(), anyString())).thenReturn(RiskLevel.IN_DANGER);

        mockMvc.perform(get("/api/risk/3")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("IN_DANGER"));
    }

    @Test
    void getRisk_shouldReturnEarlyOnset() throws Exception {
        when(riskService.assess(anyLong(), anyString())).thenReturn(RiskLevel.EARLY_ONSET);

        mockMvc.perform(get("/api/risk/4")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("EARLY_ONSET"));
    }

    @Test
    void getRisk_forwardsBearerTokenToService() throws Exception {
        when(riskService.assess(1L, "Bearer my-jwt")).thenReturn(RiskLevel.NONE);

        mockMvc.perform(get("/api/risk/1")
                        .header("Authorization", "Bearer my-jwt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskLevel").value("NONE"));
    }
}

