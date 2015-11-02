package com.catalyst.express.repository;

import com.catalyst.express.domain.Ingredient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ingredient entity.
 */
public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

}
