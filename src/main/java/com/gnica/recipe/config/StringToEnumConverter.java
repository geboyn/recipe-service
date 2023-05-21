package com.gnica.recipe.config;

import com.gnica.recipe.dto.RecipeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, RecipeType> {
    @Override
    public RecipeType convert(@NonNull String source) {
        return RecipeType.getValue(source);
    }
}
