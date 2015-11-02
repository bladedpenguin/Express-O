package com.catalyst.express.repository;

import com.catalyst.express.domain.Allergy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Allergy entity.
 */
public interface AllergyRepository extends JpaRepository<Allergy,Long> {

}
