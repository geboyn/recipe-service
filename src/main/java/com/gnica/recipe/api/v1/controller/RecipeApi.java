package com.gnica.recipe.api.v1.controller;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.dto.RecipeType;
import com.gnica.recipe.exception.pojo.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Recipe", description = "Recipe management API")
public interface RecipeApi {

    /**
     * get all recipes with filtering options
     *
     * @param type               the recipe type from See {@link com.gnica.recipe.dto.RecipeType}
     * @param servings           the number of servings of the recipe
     * @param instructions       the instructions of cooking the recipe
     * @param ingredients        the ingredients the recipe must have (the recipe will contain all the ingredients specified here)
     * @param ingredientsExclude the ingredients the recipe must not have (the recipe will not contain any of the ingredients specified here)
     * @return the list of recipes
     */
    @Operation(
            summary = "Filter recipes",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "list of recipes filtered or empty list if none match the filtering criteria",
                        content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class))))
            })
    @GetMapping("/recipes")
    ResponseEntity<List<RecipeDto>> allRecipes(
            @RequestParam(required = false, name = "type") RecipeType type,
            @RequestParam(required = false, name = "servings") Integer servings,
            @RequestParam(required = false, name = "instructions") String instructions,
            @RequestParam(required = false, name = "ingredients") Set<String> ingredients,
            @RequestParam(required = false, name = "ingredientsExclude") Set<String> ingredientsExclude);

    /**
     * get recipe by id
     *
     * @param id to fetch recipe by
     * @return recipe
     */
    @Operation(
            summary = "Get recipe by id",
            responses = {
                @ApiResponse(
                        responseCode = "404",
                        description = "recipe not found",
                        content = @Content(schema = @Schema(implementation = ApiError.class))),
                @ApiResponse(
                        responseCode = "200",
                        description = "recipe found",
                        content = @Content(schema = @Schema(implementation = RecipeDto.class)))
            })
    @GetMapping("/recipes/{id}")
    ResponseEntity<RecipeDto> getRecipe(@PathVariable(name = "id") UUID id);

    /**
     * save recipe
     *
     * @param inputRecipeDto the input object
     * @return the recipe
     */
    @Operation(
            summary = "Save new recipes",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Saved new recipes",
                        content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    @PostMapping("/recipes")
    ResponseEntity<List<RecipeDto>> saveRecipes(@RequestBody List<InputRecipeDto> inputRecipeDto);

    /**
     * update recipe
     *
     * @param id             the id of the existing recipe
     * @param inputRecipeDto the request body to be used in the update
     * @return the updated recipe
     */
    @Operation(
            summary = "Update existing recipe",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Saved new recipes",
                        content = @Content(schema = @Schema(implementation = RecipeDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad request",
                        content = @Content(schema = @Schema(implementation = ApiError.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Recipe not found",
                        content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    @PutMapping("/recipes/{id}")
    ResponseEntity<RecipeDto> updateRecipe(
            @PathVariable(name = "id") UUID id, @RequestBody InputRecipeDto inputRecipeDto);

    /**
     * delete recipe by id
     *
     * @param id of the recipe
     * @return void
     */
    @Operation(
            summary = "delete an existing recipe",
            responses = {
                @ApiResponse(responseCode = "202", description = "recipe deleted"),
                @ApiResponse(responseCode = "404", description = "recipe not found")
            })
    @DeleteMapping("/recipes/{id}")
    ResponseEntity<Void> deleteRecipe(@PathVariable(name = "id") UUID id);
}
