package com.catalyst.express.repository;

import com.catalyst.express.domain.Recipe;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Recipe entity.
 */
public interface RecipeRepository extends JpaRepository<Recipe,Long> {

    @Query("select distinct recipe from Recipe recipe left join fetch recipe.ingredients")
    List<Recipe> findAllWithEagerRelationships();

    @Query("select recipe from Recipe recipe left join fetch recipe.ingredients where recipe.id =:id")
    Recipe findOneWithEagerRelationships(@Param("id") Long id);

}
