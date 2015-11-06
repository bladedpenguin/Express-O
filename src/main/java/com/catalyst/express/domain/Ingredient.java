package com.catalyst.express.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * An Ingredient. 
 */
@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1121554407876903425L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	/**
	 * The friendly name of the ingredient.
	 */
    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The average cost per unit of the ingredient. User is expected to work out the average for themselves
     */
    @NotNull
    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "unit")
    private String unit;
    
    @Column
    private Double stock;
    
    @Column
    private Float markup;

//    @ManyToMany(mappedBy = "ingredients")
//    @JsonIgnore
//    private Set<Recipe> recipes = new HashSet<>();

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

//    public Set<Recipe> getRecipes() {
//        return recipes;
//    }
//
//    public void setRecipes(Set<Recipe> recipes) {
//        this.recipes = recipes;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ingredient ingredient = (Ingredient) o;

        if ( ! Objects.equals(id, ingredient.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cost='" + cost + "'" +
            ", unit='" + unit + "'" +
            '}';
    }

	public Double getStock() {
		return stock;
	}

	public void setStock(Double stock) {
		this.stock = stock;
	}

	public Float getMarkup() {
		return markup;
	}

	public void setMarkup(Float markup) {
		this.markup = markup;
	}
}
