package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.engine.FinancingEngine;
import org.example.projet_group_with_coudy.mapper.EdTechMapper;
import org.example.projet_group_with_coudy.repository.FinancingPlanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EdTechController.class)
class EdTechValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FinancingEngine engine;

    @MockitoBean
    private EdTechMapper mapper;

    @MockitoBean
    private FinancingPlanRepository repository;

    @Test
    void rejette_un_revenu_annuel_familial_negatif_avec_400() throws Exception {
        String dossierAvecRevenuNegatif = """
                {
                  "studentId": "ETU-00789",
                  "cycleEtudes": "LICENCE",
                  "revenuAnnuelFamilial": -900000,
                  "distanceDomicileKm": 75,
                  "mentionBaccalaureat": "TRES_BIEN",
                  "redoublement": false,
                  "redoublementJustifieMedical": false
                }
                """;

        mockMvc.perform(post("/plans-financement")
                        .contentType("application/json")
                        .content(dossierAvecRevenuNegatif))
                .andExpect(status().isBadRequest());
    }
}
