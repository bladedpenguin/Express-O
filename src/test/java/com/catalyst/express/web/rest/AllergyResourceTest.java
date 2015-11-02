package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.Allergy;
import com.catalyst.express.repository.AllergyRepository;

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
 * Test class for the AllergyResource REST controller.
 *
 * @see AllergyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AllergyResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AllergyRepository allergyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAllergyMockMvc;

    private Allergy allergy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AllergyResource allergyResource = new AllergyResource();
        ReflectionTestUtils.setField(allergyResource, "allergyRepository", allergyRepository);
        this.restAllergyMockMvc = MockMvcBuilders.standaloneSetup(allergyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        allergy = new Allergy();
        allergy.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAllergy() throws Exception {
        int databaseSizeBeforeCreate = allergyRepository.findAll().size();

        // Create the Allergy

        restAllergyMockMvc.perform(post("/api/allergys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allergy)))
                .andExpect(status().isCreated());

        // Validate the Allergy in the database
        List<Allergy> allergys = allergyRepository.findAll();
        assertThat(allergys).hasSize(databaseSizeBeforeCreate + 1);
        Allergy testAllergy = allergys.get(allergys.size() - 1);
        assertThat(testAllergy.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllAllergys() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        // Get all the allergys
        restAllergyMockMvc.perform(get("/api/allergys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(allergy.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        // Get the allergy
        restAllergyMockMvc.perform(get("/api/allergys/{id}", allergy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(allergy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAllergy() throws Exception {
        // Get the allergy
        restAllergyMockMvc.perform(get("/api/allergys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

		int databaseSizeBeforeUpdate = allergyRepository.findAll().size();

        // Update the allergy
        allergy.setName(UPDATED_NAME);

        restAllergyMockMvc.perform(put("/api/allergys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(allergy)))
                .andExpect(status().isOk());

        // Validate the Allergy in the database
        List<Allergy> allergys = allergyRepository.findAll();
        assertThat(allergys).hasSize(databaseSizeBeforeUpdate);
        Allergy testAllergy = allergys.get(allergys.size() - 1);
        assertThat(testAllergy.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

		int databaseSizeBeforeDelete = allergyRepository.findAll().size();

        // Get the allergy
        restAllergyMockMvc.perform(delete("/api/allergys/{id}", allergy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Allergy> allergys = allergyRepository.findAll();
        assertThat(allergys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
