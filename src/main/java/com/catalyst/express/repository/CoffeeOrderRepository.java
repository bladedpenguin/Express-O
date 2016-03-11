package com.catalyst.express.repository;

import com.catalyst.express.domain.CoffeeOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoffeeOrder entity.
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {

}
