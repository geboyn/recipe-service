package com.gnica.recipe.exception;

import static com.gnica.recipe.exception.pojo.Errors.INVALID_PARAMETER;
import static com.gnica.recipe.exception.pojo.Errors.INVALID_RECIPE_TYPE;
import static com.gnica.recipe.exception.pojo.Errors.RECIPE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnica.recipe.exception.pojo.ApiError;
import com.gnica.recipe.service.RecipeService;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("Test handling RecipeNotFoundException")
    @Test
    void shouldHandleRecipeNotFoundException() throws Exception {
        // given
        var id = UUID.randomUUID();
        when(recipeService.findById(id))
                .thenThrow(new RecipeNotFoundException(RECIPE_NOT_FOUND.getErrorCode(), RECIPE_NOT_FOUND.getMessage()));

        // then
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{id}", id.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();

        // and
        var response = mvcResult.getResponse().getContentAsString();

        var result = objectMapper.readValue(response, ApiError.class);

        assertEquals(RECIPE_NOT_FOUND.getErrorCode(), result.getErrorCode());
        assertEquals(RECIPE_NOT_FOUND.getMessage(), result.getMessage());
    }

    @DisplayName("Test handling InvalidRecipeType")
    @Test
    void shouldHandleInvalidRecipeType() throws Exception {
        String request = IOUtils.toString(
                Objects.requireNonNull(this.getClass().getResource("/requests/invalid-request.json")),
                StandardCharsets.UTF_8);

        // then
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        // and
        var response = mvcResult.getResponse().getContentAsString();

        var result = objectMapper.readValue(response, ApiError.class);

        assertEquals(INVALID_RECIPE_TYPE.getErrorCode(), result.getErrorCode());
        assertEquals(INVALID_RECIPE_TYPE.getMessage(), result.getMessage());
    }

    @DisplayName("Test handling ethodArgumentTypeMismatchException")
    @Test
    void shouldHandleMethodArgumentTypeMismatchException() throws Exception {
        // then
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/recipes")
                        .param("servings", "jkdnas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();

        // and
        var response = mvcResult.getResponse().getContentAsString();

        var result = objectMapper.readValue(response, ApiError.class);

        assertEquals(INVALID_PARAMETER.getErrorCode(), result.getErrorCode());
    }
}
