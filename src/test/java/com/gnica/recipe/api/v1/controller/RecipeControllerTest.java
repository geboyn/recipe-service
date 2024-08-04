package com.gnica.recipe.api.v1.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnica.recipe.helper.RecipeDataFactory;
import com.gnica.recipe.mapper.RecipeMapper;
import com.gnica.recipe.model.RecipeDto;
import com.gnica.recipe.service.RecipeService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeMapper recipeMapper;

    @DisplayName("Get recipes when no recipes exist gives empty response")
    @Test
    void shouldReturnEmptyList() throws Exception {

        this.mockMvc
                .perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("[]")));
    }

    @DisplayName("Create and retrieve recipe successfully")
    @Test
    void shouldSaveRecipe() throws Exception {
        var testInputRecipeDto = List.of(RecipeDataFactory.createTestInputRecipeDto());
        var objectMapper = new ObjectMapper();

        var request = objectMapper.writeValueAsString(testInputRecipeDto);

        String contentAsString = this.mockMvc
                .perform(
                        post("/recipes").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<RecipeDto> recipes = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        this.mockMvc
                .perform(get("/recipes/{id}", recipes.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @DisplayName("Update recipe successfully")
    @Test
    void shouldUpdateRecipe() throws Exception {
        var objectMapper = new ObjectMapper();

        // given a recipe
        var recipe = RecipeDataFactory.createTestInputRecipeDto();
        final var updatedName = "New name";

        var recipeDto = recipeService.saveRecipe(recipe);

        // when an update is performed
        recipe.setName(updatedName);
        var request = objectMapper.writeValueAsString(recipe);

        var contentAsString = this.mockMvc
                .perform(put("/recipes/{id}", recipeDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var response = objectMapper.readValue(contentAsString, RecipeDto.class);

        assertEquals(updatedName, response.getName());
    }

    @DisplayName("Delete recipe successfully")
    @Test
    void shouldDeleteRecipe() throws Exception {
        var id = UUID.randomUUID();
        this.mockMvc.perform(delete("/recipes/{id}", id.toString())).andExpect(status().isAccepted());
    }
}
