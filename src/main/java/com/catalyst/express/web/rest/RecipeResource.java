package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.catalyst.express.domain.Recipe;
import com.catalyst.express.repository.RecipeRepository;
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
 * REST controller for managing Recipe.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    @Inject
    private RecipeRepository recipeRepository;

    /**
     * POST  /recipes -> Create a new recipe.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipe);
        if (recipe.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new recipe cannot already have an ID").body(null);
        }
        Recipe result = recipeRepository.save(recipe);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recipe", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipes -> Updates an existing recipe.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recipe> updateRecipe(@Valid @RequestBody Recipe recipe) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}", recipe);
        if (recipe.getId() == null) {
            return createRecipe(recipe);
        }
        Recipe result = recipeRepository.save(recipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recipe", recipe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipes -> get all the recipes.
     */
    @RequestMapping(value = "/recipes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recipe>> getAllRecipes(Pageable pageable)
        throws URISyntaxException {
        Page<Recipe> page = recipeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recipes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /recipes/:id -> get the "id" recipe.
     */
    @RequestMapping(value = "/recipes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        return Optional.ofNullable(recipeRepository.findOneWithEagerRelationships(id))
            .map(recipe -> new ResponseEntity<>(
                recipe,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recipes/:id -> delete the "id" recipe.
     */
    @RequestMapping(value = "/recipes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recipe", id.toString())).build();
    }
}
