package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.Ingredient;
import com.catalyst.express.repository.IngredientRepository;

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
 * Test class for the IngredientResource REST controller.
 *
 * @see IngredientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IngredientResourceTest {

    private static final String DEFAULT_NAME = "AA";
    private static final String UPDATED_NAME = "BB";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final String DEFAULT_UNIT = "AAAAA";
    private static final String UPDATED_UNIT = "BBBBB";

    @Inject
    private IngredientRepository ingredientRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIngredientMockMvc;

    private Ingredient ingredient;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IngredientResource ingredientResource = new IngredientResource();
        ReflectionTestUtils.setField(ingredientResource, "ingredientRepository", ingredientRepository);
        this.restIngredientMockMvc = MockMvcBuilders.standaloneSetup(ingredientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ingredient = new Ingredient();
        ingredient.setName(DEFAULT_NAME);
        ingredient.setCost(DEFAULT_COST);
        ingredient.setUnit(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void createIngredient() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();

        // Create the Ingredient

        restIngredientMockMvc.perform(post("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isCreated());

        // Validate the Ingredient in the database
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeCreate + 1);
        Ingredient testIngredient = ingredients.get(ingredients.size() - 1);
        assertThat(testIngredient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIngredient.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testIngredient.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        // set the field null
        ingredient.setName(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc.perform(post("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isBadRequest());

        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        // set the field null
        ingredient.setCost(null);

        // Create the Ingredient, which fails.

        restIngredientMockMvc.perform(post("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isBadRequest());

        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get all the ingredients
        restIngredientMockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ingredient.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", ingredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ingredient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIngredient() throws Exception {
        // Get the ingredient
        restIngredientMockMvc.perform(get("/api/ingredients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

		int databaseSizeBeforeUpdate = ingredientRepository.findAll().size();

        // Update the ingredient
        ingredient.setName(UPDATED_NAME);
        ingredient.setCost(UPDATED_COST);
        ingredient.setUnit(UPDATED_UNIT);

        restIngredientMockMvc.perform(put("/api/ingredients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ingredient)))
                .andExpect(status().isOk());

        // Validate the Ingredient in the database
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeUpdate);
        Ingredient testIngredient = ingredients.get(ingredients.size() - 1);
        assertThat(testIngredient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIngredient.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testIngredient.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void deleteIngredient() throws Exception {
        // Initialize the database
        ingredientRepository.saveAndFlush(ingredient);

		int databaseSizeBeforeDelete = ingredientRepository.findAll().size();

        // Get the ingredient
        restIngredientMockMvc.perform(delete("/api/ingredients/{id}", ingredient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ingredient> ingredients = ingredientRepository.findAll();
        assertThat(ingredients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
