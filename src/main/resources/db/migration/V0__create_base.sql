create TABLE t_recipe
(
    id uuid NOT NULL,
    name character varying(255),
    type character varying(255),
    instructions character varying(4000),
    number_of_servings integer,
    CONSTRAINT t_recipes_pkey PRIMARY KEY (id)
);


create TABLE t_ingredient
(
 id uuid NOT NULL,
 name character varying(255) NOT NULL,
 quantity character varying(255) NOT NULL,
 CONSTRAINT t_ingredients_pkey PRIMARY KEY (id)
);

CREATE TABLE t_recipe_ingredients
(
    recipe_id uuid NOT NULL,
    ingredient_id uuid NOT NULL,
    CONSTRAINT t_recipe_ingredients_pkey PRIMARY KEY (recipe_id, ingredient_id),
    CONSTRAINT fk_recipe_ingredient_recipe FOREIGN KEY (recipe_id)
        REFERENCES t_recipe (id),
    CONSTRAINT fk_recipe_ingredient_ingredient FOREIGN KEY (ingredient_id)
        REFERENCES t_ingredient (id)
);

