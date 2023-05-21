package com.gnica.recipe.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnica.recipe.helper.RecipeDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Get recipes when no recipe exists gives empty response")
    @Test
    void shouldReturnEmptyList() throws Exception {

        this.mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("[]")));
    }

    @DisplayName("Create recipe successfully")
    @Test
    void shouldSaveRecipe() throws Exception {
        var testInputRecipeDto = List.of(RecipeDataFactory.createTestInputRecipeDto());
        var objectMapper = new ObjectMapper();

        var request = objectMapper.writeValueAsString(testInputRecipeDto);

        this.mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete recipe successfully")
    @Test
    void shouldDeleteRecipe() throws Exception {
        var id = UUID.randomUUID();
        this.mockMvc.perform(delete("/recipes/{id}", id.toString()))
                .andExpect(status().isAccepted());
    }

}