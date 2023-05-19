package com.gnica.recipe.config;

import com.gnica.recipe.dto.InputRecipeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, InputRecipeDto.RecipeType> {
    @Override
    public InputRecipeDto.RecipeType convert(@NonNull String source) {
        return InputRecipeDto.RecipeType.getValueOrDefault(source);
    }
}
