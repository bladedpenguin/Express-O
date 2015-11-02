package com.catalyst.express.repository;

import com.catalyst.express.domain.Muffin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Muffin entity.
 */
public interface MuffinRepository extends JpaRepository<Muffin,Long> {

}
