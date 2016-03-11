package com.catalyst.express.web.rest;

import com.catalyst.express.Application;
import com.catalyst.express.domain.CoffeeOrder;
import com.catalyst.express.repository.CoffeeOrderRepository;

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
 * Test class for the CoffeeOrderResource REST controller.
 *
 * @see CoffeeOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CoffeeOrderResourceTest {


    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;
    private static final String DEFAULT_CUSTOMER_NAME = "";
    private static final String UPDATED_CUSTOMER_NAME = "";

    @Inject
    private CoffeeOrderRepository coffeeOrderRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoffeeOrderMockMvc;

    private CoffeeOrder coffeeOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoffeeOrderResource coffeeOrderResource = new CoffeeOrderResource();
        ReflectionTestUtils.setField(coffeeOrderResource, "coffeeOrderRepository", coffeeOrderRepository);
        this.restCoffeeOrderMockMvc = MockMvcBuilders.standaloneSetup(coffeeOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coffeeOrder = new CoffeeOrder();
        coffeeOrder.setQuantity(DEFAULT_QUANTITY);
        coffeeOrder.setCustomerName(DEFAULT_CUSTOMER_NAME);
    }

//    @Test
//    @Transactional
//    public void createCoffeeOrder() throws Exception {
//        int databaseSizeBeforeCreate = coffeeOrderRepository.findAll().size();
//
//        // Create the CoffeeOrder
//
//        restCoffeeOrderMockMvc.perform(post("/api/coffeeOrders")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(coffeeOrder)))
//                .andExpect(status().isCreated());
//
//        // Validate the CoffeeOrder in the database
//        List<CoffeeOrder> coffeeOrders = coffeeOrderRepository.findAll();
//        assertThat(coffeeOrders).hasSize(databaseSizeBeforeCreate + 1);
//        CoffeeOrder testCoffeeOrder = coffeeOrders.get(coffeeOrders.size() - 1);
//        assertThat(testCoffeeOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
//        assertThat(testCoffeeOrder.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
//    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = coffeeOrderRepository.findAll().size();
        // set the field null
        coffeeOrder.setQuantity(null);

        // Create the CoffeeOrder, which fails.

        restCoffeeOrderMockMvc.perform(post("/api/coffeeOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coffeeOrder)))
                .andExpect(status().isBadRequest());

        List<CoffeeOrder> coffeeOrders = coffeeOrderRepository.findAll();
        assertThat(coffeeOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = coffeeOrderRepository.findAll().size();
        // set the field null
        coffeeOrder.setCustomerName(null);

        // Create the CoffeeOrder, which fails.

        restCoffeeOrderMockMvc.perform(post("/api/coffeeOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coffeeOrder)))
                .andExpect(status().isBadRequest());

        List<CoffeeOrder> coffeeOrders = coffeeOrderRepository.findAll();
        assertThat(coffeeOrders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCoffeeOrders() throws Exception {
        // Initialize the database
        coffeeOrderRepository.saveAndFlush(coffeeOrder);

        // Get all the coffeeOrders
        restCoffeeOrderMockMvc.perform(get("/api/coffeeOrders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coffeeOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCoffeeOrder() throws Exception {
        // Initialize the database
        coffeeOrderRepository.saveAndFlush(coffeeOrder);

        // Get the coffeeOrder
        restCoffeeOrderMockMvc.perform(get("/api/coffeeOrders/{id}", coffeeOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coffeeOrder.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoffeeOrder() throws Exception {
        // Get the coffeeOrder
        restCoffeeOrderMockMvc.perform(get("/api/coffeeOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

//    @Test
//    @Transactional
//    public void updateCoffeeOrder() throws Exception {
//        // Initialize the database
//        coffeeOrderRepository.saveAndFlush(coffeeOrder);
//
//		int databaseSizeBeforeUpdate = coffeeOrderRepository.findAll().size();
//
//        // Update the coffeeOrder
//        coffeeOrder.setQuantity(UPDATED_QUANTITY);
//        coffeeOrder.setCustomerName(UPDATED_CUSTOMER_NAME);
//
//        restCoffeeOrderMockMvc.perform(put("/api/coffeeOrders")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(coffeeOrder)))
//                .andExpect(status().isOk());
//
//        // Validate the CoffeeOrder in the database
//        List<CoffeeOrder> coffeeOrders = coffeeOrderRepository.findAll();
//        assertThat(coffeeOrders).hasSize(databaseSizeBeforeUpdate);
//        CoffeeOrder testCoffeeOrder = coffeeOrders.get(coffeeOrders.size() - 1);
//        assertThat(testCoffeeOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
//        assertThat(testCoffeeOrder.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
//    }

//    @Test
//    @Transactional
//    public void deleteCoffeeOrder() throws Exception {
//        // Initialize the database
//        coffeeOrderRepository.saveAndFlush(coffeeOrder);
//
//		int databaseSizeBeforeDelete = coffeeOrderRepository.findAll().size();
//
//        // Get the coffeeOrder
//        restCoffeeOrderMockMvc.perform(delete("/api/coffeeOrders/{id}", coffeeOrder.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<CoffeeOrder> coffeeOrders = coffeeOrderRepository.findAll();
//        assertThat(coffeeOrders).hasSize(databaseSizeBeforeDelete - 1);
//    }
}
