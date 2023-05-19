package com.gnica.recipe.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.helper.RecipeDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.runtime.ObjectMethods;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @DisplayName("Get recipes when no recipe exists gives empty response")
    @Test
    void test() throws Exception {
        var testInputRecipeDto = RecipeDataFactory.createTestInputRecipeDto();
        var objectMapper = new ObjectMapper();

        var request = objectMapper.writeValueAsString(testInputRecipeDto);

        this.mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is(testInputRecipeDto.getDescription())))
                .andExpect(jsonPath("$.recipeType", is(testInputRecipeDto.getRecipeType().toString())))
                .andExpect(jsonPath("$.numberOfServings", is(testInputRecipeDto.getNumberOfServings())));
    }
}