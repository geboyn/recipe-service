package com.gnica.recipe.mapper;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.entity.Recipe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    Recipe toEntity(InputRecipeDto source);

    List<Recipe> toEntities(List<InputRecipeDto> source);


    RecipeDto fromEntity(Recipe recipe);

    List<RecipeDto> fromEntities(List<Recipe> recipies);


}
