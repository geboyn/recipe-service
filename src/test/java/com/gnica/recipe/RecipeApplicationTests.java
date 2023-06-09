package com.gnica.recipe;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gnica.recipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipeApplicationTests {

    @Autowired
    private RecipeService recipeService;

    @Test
    void contextLoads() {
        assertNotNull(recipeService);
    }
}
