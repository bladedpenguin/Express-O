package com.catalyst.express.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A IngredientQuantity.
 */
@Entity
@Table(name = "ingredient_quantity")
public class IngredientQuantity implements Serializable {

	private static final long serialVersionUID = -3188277458094242839L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    
    @ManyToOne(fetch = FetchType.EAGER)
    private Ingredient ingredient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngredientQuantity ingredientQuantity = (IngredientQuantity) o;

        if ( ! Objects.equals(id, ingredientQuantity.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IngredientQuantity{" +
            "id=" + id +
            ", quantity='" + quantity + "', ingredient=" + ingredient +
            '}';
    }
}
