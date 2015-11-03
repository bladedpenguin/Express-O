package com.catalyst.express.repository;

import com.catalyst.express.domain.Allergen;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Allergen entity.
 */
public interface AllergenRepository extends JpaRepository<Allergen,Long> {

}
