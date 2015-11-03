package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catalyst.express.domain.Allergen;
import com.catalyst.express.repository.AllergenRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Allergen.
 */
@RestController
@RequestMapping("/api")
public class AllergenResource {

    private final Logger log = LoggerFactory.getLogger(AllergenResource.class);

    @Inject
    private AllergenRepository allergenRepository;

    /**
     * POST  /allergens -> Create a new allergen.
     */
    @RequestMapping(value = "/allergens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergen> createAllergen(@RequestBody Allergen allergen) throws URISyntaxException {
        log.debug("REST request to save Allergen : {}", allergen);
        if (allergen.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new allergen cannot already have an ID").body(null);
        }
        Allergen result = allergenRepository.save(allergen);
        return ResponseEntity.created(new URI("/api/allergens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("allergen", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /allergens -> Updates an existing allergen.
     */
    @RequestMapping(value = "/allergens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergen> updateAllergen(@RequestBody Allergen allergen) throws URISyntaxException {
        log.debug("REST request to update Allergen : {}", allergen);
        if (allergen.getId() == null) {
            return createAllergen(allergen);
        }
        Allergen result = allergenRepository.save(allergen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("allergen", allergen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /allergens -> get all the allergens.
     */
    @RequestMapping(value = "/allergens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Allergen>> getAllAllergens(Pageable pageable)
        throws URISyntaxException {
        Page<Allergen> page = allergenRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/allergens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /allergens/:id -> get the "id" allergen.
     */
    @RequestMapping(value = "/allergens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergen> getAllergen(@PathVariable Long id) {
        log.debug("REST request to get Allergen : {}", id);
        return Optional.ofNullable(allergenRepository.findOne(id))
            .map(allergen -> new ResponseEntity<>(
                allergen,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /allergens/:id -> delete the "id" allergen.
     */
    @RequestMapping(value = "/allergens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAllergen(@PathVariable Long id) {
        log.debug("REST request to delete Allergen : {}", id);
        allergenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("allergen", id.toString())).build();
    }
}
