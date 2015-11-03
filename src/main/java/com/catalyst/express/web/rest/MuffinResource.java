package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catalyst.express.domain.Muffin;
import com.catalyst.express.repository.MuffinRepository;
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
 * REST controller for managing Muffin.
 */
@RestController
@RequestMapping("/api")
public class MuffinResource {

    private final Logger log = LoggerFactory.getLogger(MuffinResource.class);

    @Inject
    private MuffinRepository muffinRepository;

    /**
     * POST  /muffins -> Create a new muffin.
     */
    @RequestMapping(value = "/muffins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Muffin> createMuffin(@Valid @RequestBody Muffin muffin) throws URISyntaxException {
        log.debug("REST request to save Muffin : {}", muffin);
        if (muffin.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new muffin cannot already have an ID").body(null);
        }
        Muffin result = muffinRepository.save(muffin);
        return ResponseEntity.created(new URI("/api/muffins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("muffin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /muffins -> Updates an existing muffin.
     */
    @RequestMapping(value = "/muffins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Muffin> updateMuffin(@Valid @RequestBody Muffin muffin) throws URISyntaxException {
        log.debug("REST request to update Muffin : {}", muffin);
        if (muffin.getId() == null) {
            return createMuffin(muffin);
        }
        Muffin result = muffinRepository.save(muffin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("muffin", muffin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /muffins -> get all the muffins.
     */
    @RequestMapping(value = "/muffins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Muffin>> getAllMuffins(Pageable pageable)
        throws URISyntaxException {
        Page<Muffin> page = muffinRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/muffins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /muffins/:id -> get the "id" muffin.
     */
    @RequestMapping(value = "/muffins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Muffin> getMuffin(@PathVariable Long id) {
        log.debug("REST request to get Muffin : {}", id);
        return Optional.ofNullable(muffinRepository.findOneWithEagerRelationships(id))
            .map(muffin -> new ResponseEntity<>(
                muffin,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /muffins/:id -> delete the "id" muffin.
     */
    @RequestMapping(value = "/muffins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMuffin(@PathVariable Long id) {
        log.debug("REST request to delete Muffin : {}", id);
        muffinRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("muffin", id.toString())).build();
    }
}
