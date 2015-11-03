package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.Allergen;
import com.catalyst.express.repository.AllergenRepository;

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
 * Test class for the AllergenResource REST controller.
 *
 * @see AllergenResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AllergenResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AllergenRepository allergenRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAllergenMockMvc;

    private Allergen allergen;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AllergenResource allergenResource = new AllergenResource();
        ReflectionTestUtils.setField(allergenResource, "allergenRepository", allergenRepository);
        this.restAllergenMockMvc = MockMvcBuilders.standaloneSetup(allergenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        allergen = new Allergen();
        allergen.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAllergen() throws Exception {
        int databaseSizeBeforeCreate = allergenRepository.findAll().size();

        // Create the Allergen

        restAllergenMockMvc.perform(post("/api/allergens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allergen)))
                .andExpect(status().isCreated());

        // Validate the Allergen in the database
        List<Allergen> allergens = allergenRepository.findAll();
        assertThat(allergens).hasSize(databaseSizeBeforeCreate + 1);
        Allergen testAllergen = allergens.get(allergens.size() - 1);
        assertThat(testAllergen.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllAllergens() throws Exception {
        // Initialize the database
        allergenRepository.saveAndFlush(allergen);

        // Get all the allergens
        restAllergenMockMvc.perform(get("/api/allergens"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(allergen.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAllergen() throws Exception {
        // Initialize the database
        allergenRepository.saveAndFlush(allergen);

        // Get the allergen
        restAllergenMockMvc.perform(get("/api/allergens/{id}", allergen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(allergen.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAllergen() throws Exception {
        // Get the allergen
        restAllergenMockMvc.perform(get("/api/allergens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAllergen() throws Exception {
        // Initialize the database
        allergenRepository.saveAndFlush(allergen);

		int databaseSizeBeforeUpdate = allergenRepository.findAll().size();

        // Update the allergen
        allergen.setName(UPDATED_NAME);

        restAllergenMockMvc.perform(put("/api/allergens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allergen)))
                .andExpect(status().isOk());

        // Validate the Allergen in the database
        List<Allergen> allergens = allergenRepository.findAll();
        assertThat(allergens).hasSize(databaseSizeBeforeUpdate);
        Allergen testAllergen = allergens.get(allergens.size() - 1);
        assertThat(testAllergen.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteAllergen() throws Exception {
        // Initialize the database
        allergenRepository.saveAndFlush(allergen);

		int databaseSizeBeforeDelete = allergenRepository.findAll().size();

        // Get the allergen
        restAllergenMockMvc.perform(delete("/api/allergens/{id}", allergen.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Allergen> allergens = allergenRepository.findAll();
        assertThat(allergens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
