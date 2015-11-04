package com.catalyst.express.repository;

import com.catalyst.express.domain.IngredientQuantity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the IngredientQuantity entity.
 */
public interface IngredientQuantityRepository extends JpaRepository<IngredientQuantity,Long> {

}
