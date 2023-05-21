package com.gnica.recipe.api.v1.controller;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Recipe", description = "Recipe management API")
public interface RecipeApi {

    @GetMapping("/recipes")
    ResponseEntity<List<RecipeDto>> getRecipes(HttpServletRequest request);

    @GetMapping("/recipes/{id}")
    ResponseEntity<RecipeDto> getRecipe(@PathVariable UUID id);


    @Operation(
            summary = "Save new recipe",
            tags = {"save", "recipe"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = RecipeDto.class), mediaType = "application/json")})
    })
    @PostMapping("/recipes")
    ResponseEntity<List<RecipeDto>> saveRecipes(@RequestBody List<InputRecipeDto> inputRecipeDto);

    @DeleteMapping("/recipes/{id}")
    ResponseEntity<Void> deleteRecipe(@PathVariable UUID id);
}
