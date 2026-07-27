package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.engine.AgriculturalSubsidyEngine;
import org.example.projet_group_with_coudy.mapper.AgriTechMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgriTechController.class)
class AgriTechValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgriculturalSubsidyEngine engine;

    @MockitoBean
    private AgriTechMapper mapper;

    @Test
    void rejette_une_surface_en_hectares_negative_avec_400() throws Exception {
        String declarationAvecHectaresNegatifs = """
                {
                  "farmId": "FARM-00456",
                  "hectares": -12.5,
                  "typeCulture": "MIL",
                  "certificationBiologique": true,
                  "rendementDeclareParHectare": 600,
                  "localisation": "Kaffrine"
                }
                """;

        mockMvc.perform(post("/allocations-subvention")
                        .contentType("application/json")
                        .content(declarationAvecHectaresNegatifs))
                .andExpect(status().isBadRequest());
    }
}
