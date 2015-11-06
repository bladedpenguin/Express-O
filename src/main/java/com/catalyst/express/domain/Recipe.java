package com.catalyst.express.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Recipe implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8916447238819308177L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn
    @JsonManagedReference
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    private List<IngredientQuantity> ingredients = new ArrayList<>();
    
    @Column
    private Double markup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientQuantity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientQuantity> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Recipe recipe = (Recipe) o;

        if ( ! Objects.equals(id, recipe.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + id +
            ", name='" + name + "', ingredients=" + ingredients +
            '}';
    }

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}
}
