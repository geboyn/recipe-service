package com.gnica.recipe.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity(name = "t_recipe")
public class Recipe {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "description")
    private String description;

    //@Enumerated(EnumType.STRING)
    @Column(name = "recipe_type")
    private String recipeType;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "number_of_servings")
    private int numberOfServings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ingredient> ingredients;
}
