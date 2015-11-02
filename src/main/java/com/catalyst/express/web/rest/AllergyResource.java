package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catalyst.express.domain.Allergy;
import com.catalyst.express.repository.AllergyRepository;
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
 * REST controller for managing Allergy.
 */
@RestController
@RequestMapping("/api")
public class AllergyResource {

    private final Logger log = LoggerFactory.getLogger(AllergyResource.class);

    @Inject
    private AllergyRepository allergyRepository;

    /**
     * POST  /allergys -> Create a new allergy.
     */
    @RequestMapping(value = "/allergys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergy> createAllergy(@RequestBody Allergy allergy) throws URISyntaxException {
        log.debug("REST request to save Allergy : {}", allergy);
        if (allergy.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new allergy cannot already have an ID").body(null);
        }
        Allergy result = allergyRepository.save(allergy);
        return ResponseEntity.created(new URI("/api/allergys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("allergy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /allergys -> Updates an existing allergy.
     */
    @RequestMapping(value = "/allergys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergy> updateAllergy(@RequestBody Allergy allergy) throws URISyntaxException {
        log.debug("REST request to update Allergy : {}", allergy);
        if (allergy.getId() == null) {
            return createAllergy(allergy);
        }
        Allergy result = allergyRepository.save(allergy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("allergy", allergy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /allergys -> get all the allergys.
     */
    @RequestMapping(value = "/allergys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Allergy>> getAllAllergys(Pageable pageable)
        throws URISyntaxException {
        Page<Allergy> page = allergyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/allergys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /allergys/:id -> get the "id" allergy.
     */
    @RequestMapping(value = "/allergys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Allergy> getAllergy(@PathVariable Long id) {
        log.debug("REST request to get Allergy : {}", id);
        return Optional.ofNullable(allergyRepository.findOne(id))
            .map(allergy -> new ResponseEntity<>(
                allergy,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /allergys/:id -> delete the "id" allergy.
     */
    @RequestMapping(value = "/allergys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long id) {
        log.debug("REST request to delete Allergy : {}", id);
        allergyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("allergy", id.toString())).build();
    }
}
