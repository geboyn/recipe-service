package com.gnica.recipe.service;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.entity.Recipe;
import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import com.gnica.recipe.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeMapper mapper;
    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public RecipeDto saveRecipe(InputRecipeDto inputRecipeDto) {
        var entity = mapper.toEntity(inputRecipeDto);
        var savedEntity = recipeRepository.save(entity);

        return mapper.fromEntity(savedEntity);
    }

    @Override
    @Transactional
    public RecipeDto findById(UUID id) {
        return recipeRepository.findById(id)
                .map(mapper::fromEntity).orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    @Transactional
    public List<RecipeDto> findAll() {
        List<Recipe> all = recipeRepository.findAll();
        return mapper.fromEntities(all);
    }

    @Override
    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }
}
