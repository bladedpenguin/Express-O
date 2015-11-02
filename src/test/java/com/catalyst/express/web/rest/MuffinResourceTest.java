package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.Muffin;
import com.catalyst.express.repository.MuffinRepository;

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
 * Test class for the MuffinResource REST controller.
 *
 * @see MuffinResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MuffinResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_COST = 0D;
    private static final Double UPDATED_COST = 1D;
    private static final String DEFAULT_VENDOR = "AAAAA";
    private static final String UPDATED_VENDOR = "BBBBB";

    @Inject
    private MuffinRepository muffinRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMuffinMockMvc;

    private Muffin muffin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MuffinResource muffinResource = new MuffinResource();
        ReflectionTestUtils.setField(muffinResource, "muffinRepository", muffinRepository);
        this.restMuffinMockMvc = MockMvcBuilders.standaloneSetup(muffinResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        muffin = new Muffin();
        muffin.setName(DEFAULT_NAME);
        muffin.setCost(DEFAULT_COST);
        muffin.setVendor(DEFAULT_VENDOR);
    }

    @Test
    @Transactional
    public void createMuffin() throws Exception {
        int databaseSizeBeforeCreate = muffinRepository.findAll().size();

        // Create the Muffin

        restMuffinMockMvc.perform(post("/api/muffins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(muffin)))
                .andExpect(status().isCreated());

        // Validate the Muffin in the database
        List<Muffin> muffins = muffinRepository.findAll();
        assertThat(muffins).hasSize(databaseSizeBeforeCreate + 1);
        Muffin testMuffin = muffins.get(muffins.size() - 1);
        assertThat(testMuffin.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMuffin.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testMuffin.getVendor()).isEqualTo(DEFAULT_VENDOR);
    }

    @Test
    @Transactional
    public void getAllMuffins() throws Exception {
        // Initialize the database
        muffinRepository.saveAndFlush(muffin);

        // Get all the muffins
        restMuffinMockMvc.perform(get("/api/muffins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(muffin.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
                .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.toString())));
    }

    @Test
    @Transactional
    public void getMuffin() throws Exception {
        // Initialize the database
        muffinRepository.saveAndFlush(muffin);

        // Get the muffin
        restMuffinMockMvc.perform(get("/api/muffins/{id}", muffin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(muffin.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMuffin() throws Exception {
        // Get the muffin
        restMuffinMockMvc.perform(get("/api/muffins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMuffin() throws Exception {
        // Initialize the database
        muffinRepository.saveAndFlush(muffin);

		int databaseSizeBeforeUpdate = muffinRepository.findAll().size();

        // Update the muffin
        muffin.setName(UPDATED_NAME);
        muffin.setCost(UPDATED_COST);
        muffin.setVendor(UPDATED_VENDOR);

        restMuffinMockMvc.perform(put("/api/muffins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(muffin)))
                .andExpect(status().isOk());

        // Validate the Muffin in the database
        List<Muffin> muffins = muffinRepository.findAll();
        assertThat(muffins).hasSize(databaseSizeBeforeUpdate);
        Muffin testMuffin = muffins.get(muffins.size() - 1);
        assertThat(testMuffin.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMuffin.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testMuffin.getVendor()).isEqualTo(UPDATED_VENDOR);
    }

    @Test
    @Transactional
    public void deleteMuffin() throws Exception {
        // Initialize the database
        muffinRepository.saveAndFlush(muffin);

		int databaseSizeBeforeDelete = muffinRepository.findAll().size();

        // Get the muffin
        restMuffinMockMvc.perform(delete("/api/muffins/{id}", muffin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Muffin> muffins = muffinRepository.findAll();
        assertThat(muffins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
