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
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Recipe", description = "Recipe management API")
public interface RecipeApi {

    /**
     * get all recipes with filtering options
     *
     * @param request the request with the filtering parameters. See {@link com.gnica.recipe.search.SearchParameters}
     * @return the list of recipes
     */
    @GetMapping("/recipes")
    ResponseEntity<List<RecipeDto>> allRecipes(HttpServletRequest request);

    /**
     * get recipe by id
     *
     * @param id to fetch recipe by
     * @return recipe
     */
    @GetMapping("/recipes/{id}")
    ResponseEntity<RecipeDto> getRecipe(@PathVariable UUID id);

    /**
     * save recipe
     *
     * @param inputRecipeDto the input object
     * @return the recipe
     */
    @Operation(
            summary = "Save new recipe",
            tags = {"save", "recipe"})
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        content = {
                            @Content(schema = @Schema(implementation = RecipeDto.class), mediaType = "application/json")
                        })
            })
    @PostMapping("/recipes")
    ResponseEntity<List<RecipeDto>> saveRecipes(@RequestBody List<InputRecipeDto> inputRecipeDto);

    /**
     * delete recipe by id
     *
     * @param id of the recipe
     * @return void
     */
    @DeleteMapping("/recipes/{id}")
    ResponseEntity<Void> deleteRecipe(@PathVariable UUID id);
}
