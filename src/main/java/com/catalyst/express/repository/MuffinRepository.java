package com.catalyst.express.repository;

import com.catalyst.express.domain.Muffin;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Muffin entity.
 */
public interface MuffinRepository extends JpaRepository<Muffin,Long> {

    @Query("select distinct muffin from Muffin muffin left join fetch muffin.allergens")
    List<Muffin> findAllWithEagerRelationships();

    @Query("select muffin from Muffin muffin left join fetch muffin.allergens where muffin.id =:id")
    Muffin findOneWithEagerRelationships(@Param("id") Long id);

}
