package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catalyst.express.domain.IngredientQuantity;
import com.catalyst.express.repository.IngredientQuantityRepository;
import com.catalyst.express.web.rest.util.HeaderUtil;
import com.catalyst.express.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing IngredientQuantity.
 */
@RestController
@RequestMapping("/api")
public class IngredientQuantityResource {

    private final Logger log = LoggerFactory.getLogger(IngredientQuantityResource.class);

    @Inject
    private IngredientQuantityRepository ingredientQuantityRepository;

    /**
     * POST  /ingredientQuantitys -> Create a new ingredientQuantity.
     */
    @RequestMapping(value = "/ingredientQuantitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IngredientQuantity> createIngredientQuantity(@Valid @RequestBody IngredientQuantity ingredientQuantity) throws URISyntaxException {
        log.debug("REST request to save IngredientQuantity : {}", ingredientQuantity);
        if (ingredientQuantity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ingredientQuantity cannot already have an ID").body(null);
        }
        IngredientQuantity result = ingredientQuantityRepository.save(ingredientQuantity);
        return ResponseEntity.created(new URI("/api/ingredientQuantitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ingredientQuantity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ingredientQuantitys -> Updates an existing ingredientQuantity.
     */
    @RequestMapping(value = "/ingredientQuantitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IngredientQuantity> updateIngredientQuantity(@Valid @RequestBody IngredientQuantity ingredientQuantity) throws URISyntaxException {
        log.debug("REST request to update IngredientQuantity : {}", ingredientQuantity);
        if (ingredientQuantity.getId() == null) {
            return createIngredientQuantity(ingredientQuantity);
        }
        IngredientQuantity result = ingredientQuantityRepository.save(ingredientQuantity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ingredientQuantity", ingredientQuantity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ingredientQuantitys -> get all the ingredientQuantitys.
     */
    @RequestMapping(value = "/ingredientQuantitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<IngredientQuantity>> getAllIngredientQuantitys(Pageable pageable)
        throws URISyntaxException {
        Page<IngredientQuantity> page = ingredientQuantityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ingredientQuantitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ingredientQuantitys/:id -> get the "id" ingredientQuantity.
     */
    @RequestMapping(value = "/ingredientQuantitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IngredientQuantity> getIngredientQuantity(@PathVariable Long id) {
        log.debug("REST request to get IngredientQuantity : {}", id);
        return Optional.ofNullable(ingredientQuantityRepository.findOne(id))
            .map(ingredientQuantity -> new ResponseEntity<>(
                ingredientQuantity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ingredientQuantitys/:id -> delete the "id" ingredientQuantity.
     */
    @RequestMapping(value = "/ingredientQuantitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIngredientQuantity(@PathVariable Long id) {
        log.debug("REST request to delete IngredientQuantity : {}", id);
        ingredientQuantityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ingredientQuantity", id.toString())).build();
    }
}
