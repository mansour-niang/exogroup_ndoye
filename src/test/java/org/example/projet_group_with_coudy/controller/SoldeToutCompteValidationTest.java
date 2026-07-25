package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.engine.SoldeToutCompteEngine;
import org.example.projet_group_with_coudy.mapper.SoldeToutCompteMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SoldeToutCompteController.class)
class SoldeToutCompteValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SoldeToutCompteEngine engine;

    @MockitoBean
    private SoldeToutCompteMapper mapper;

    @Test
    void rejette_un_salaire_mensuel_de_base_negatif_avec_400() throws Exception {
        String dossierAvecSalaireNegatif = """
                {
                  "employeeId": "EMP-00123",
                  "dateDebutContrat": "2026-07-25",
                  "dateFinContrat": "2026-07-25",
                  "motifDepart": "DEMISSION",
                  "salaireMensuelBase": -450000,
                  "joursCongesRestants": 12,
                  "preavisRespecte": true
                }
                """;

        mockMvc.perform(post("/soldes-tout-compte")
                        .contentType("application/json")
                        .content(dossierAvecSalaireNegatif))
                .andExpect(status().isBadRequest());
    }
}
