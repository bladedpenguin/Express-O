package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.IngredientQuantity;
import com.catalyst.express.repository.IngredientQuantityRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IngredientQuantityResource REST controller.
 *
 * @see IngredientQuantityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IngredientQuantityResourceTest {


    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    @Inject
    private IngredientQuantityRepository ingredientQuantityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIngredientQuantityMockMvc;

    private IngredientQuantity ingredientQuantity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IngredientQuantityResource ingredientQuantityResource = new IngredientQuantityResource();
        ReflectionTestUtils.setField(ingredientQuantityResource, "ingredientQuantityRepository", ingredientQuantityRepository);
        this.restIngredientQuantityMockMvc = MockMvcBuilders.standaloneSetup(ingredientQuantityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ingredientQuantity = new IngredientQuantity();
        ingredientQuantity.setQuantity(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createIngredientQuantity() throws Exception {
        int databaseSizeBeforeCreate = ingredientQuantityRepository.findAll().size();

        // Create the IngredientQuantity

        restIngredientQuantityMockMvc.perform(post("/api/ingredientQuantitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientQuantity)))
                .andExpect(status().isCreated());

        // Validate the IngredientQuantity in the database
        List<IngredientQuantity> ingredientQuantitys = ingredientQuantityRepository.findAll();
        assertThat(ingredientQuantitys).hasSize(databaseSizeBeforeCreate + 1);
        IngredientQuantity testIngredientQuantity = ingredientQuantitys.get(ingredientQuantitys.size() - 1);
        assertThat(testIngredientQuantity.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientQuantityRepository.findAll().size();
        // set the field null
        ingredientQuantity.setQuantity(null);

        // Create the IngredientQuantity, which fails.

        restIngredientQuantityMockMvc.perform(post("/api/ingredientQuantitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientQuantity)))
                .andExpect(status().isBadRequest());

        List<IngredientQuantity> ingredientQuantitys = ingredientQuantityRepository.findAll();
        assertThat(ingredientQuantitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIngredientQuantitys() throws Exception {
        // Initialize the database
        ingredientQuantityRepository.saveAndFlush(ingredientQuantity);

        // Get all the ingredientQuantitys
        restIngredientQuantityMockMvc.perform(get("/api/ingredientQuantitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ingredientQuantity.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())));
    }

    @Test
    @Transactional
    public void getIngredientQuantity() throws Exception {
        // Initialize the database
        ingredientQuantityRepository.saveAndFlush(ingredientQuantity);

        // Get the ingredientQuantity
        restIngredientQuantityMockMvc.perform(get("/api/ingredientQuantitys/{id}", ingredientQuantity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ingredientQuantity.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIngredientQuantity() throws Exception {
        // Get the ingredientQuantity
        restIngredientQuantityMockMvc.perform(get("/api/ingredientQuantitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngredientQuantity() throws Exception {
        // Initialize the database
        ingredientQuantityRepository.saveAndFlush(ingredientQuantity);

		int databaseSizeBeforeUpdate = ingredientQuantityRepository.findAll().size();

        // Update the ingredientQuantity
        ingredientQuantity.setQuantity(UPDATED_QUANTITY);

        restIngredientQuantityMockMvc.perform(put("/api/ingredientQuantitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredientQuantity)))
                .andExpect(status().isOk());

        // Validate the IngredientQuantity in the database
        List<IngredientQuantity> ingredientQuantitys = ingredientQuantityRepository.findAll();
        assertThat(ingredientQuantitys).hasSize(databaseSizeBeforeUpdate);
        IngredientQuantity testIngredientQuantity = ingredientQuantitys.get(ingredientQuantitys.size() - 1);
        assertThat(testIngredientQuantity.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteIngredientQuantity() throws Exception {
        // Initialize the database
        ingredientQuantityRepository.saveAndFlush(ingredientQuantity);

		int databaseSizeBeforeDelete = ingredientQuantityRepository.findAll().size();

        // Get the ingredientQuantity
        restIngredientQuantityMockMvc.perform(delete("/api/ingredientQuantitys/{id}", ingredientQuantity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<IngredientQuantity> ingredientQuantitys = ingredientQuantityRepository.findAll();
        assertThat(ingredientQuantitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
