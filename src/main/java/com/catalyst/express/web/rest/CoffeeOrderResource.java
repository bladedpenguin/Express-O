package com.catalyst.express.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.catalyst.express.domain.CoffeeOrder;
import com.catalyst.express.repository.CoffeeOrderRepository;
import com.catalyst.express.service.SqsService;
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
 * REST controller for managing CoffeeOrder.
 */
@RestController
@RequestMapping("/api")
public class CoffeeOrderResource {

    private final Logger log = LoggerFactory.getLogger(CoffeeOrderResource.class);
    

    @Inject
    private CoffeeOrderRepository coffeeOrderRepository;
    
    @Inject
    private SqsService sqs;

    /**
     * POST  /coffeeOrders -> Create a new coffeeOrder.
     */
    @RequestMapping(value = "/coffeeOrders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoffeeOrder> createCoffeeOrder(@Valid @RequestBody CoffeeOrder coffeeOrder) throws URISyntaxException {
        log.debug("REST request to save CoffeeOrder : {}", coffeeOrder);
        if (coffeeOrder.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new coffeeOrder cannot already have an ID").body(null);
        }
//        CoffeeOrder result = coffeeOrderRepository.save(coffeeOrder);
        //AmazonSQSClient sqs = new AmazonSQSClient();
        sqs.orderCoffee(coffeeOrder);
        ResponseEntity<CoffeeOrder> re = ResponseEntity.ok(coffeeOrder);
        //sqs.sendMessage("", coffeeOrder.toString());
        
        return re;
//        return ResponseEntity.created(new URI("/api/coffeeOrders/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert("coffeeOrder", result.getId().toString()))
//            .body(coffeeOrder);
    }

    /**
     * PUT  /coffeeOrders -> Updates an existing coffeeOrder.
     */
//    @RequestMapping(value = "/coffeeOrders",
//        method = RequestMethod.PUT,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<CoffeeOrder> updateCoffeeOrder(@Valid @RequestBody CoffeeOrder coffeeOrder) throws URISyntaxException {
//        log.debug("REST request to update CoffeeOrder : {}", coffeeOrder);
//        if (coffeeOrder.getId() == null) {
//            return createCoffeeOrder(coffeeOrder);
//        }
//        CoffeeOrder result = coffeeOrderRepository.save(coffeeOrder);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert("coffeeOrder", coffeeOrder.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /coffeeOrders -> get all the coffeeOrders.
     */
    @RequestMapping(value = "/coffeeOrders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CoffeeOrder>> getAllCoffeeOrders(Pageable pageable)
        throws URISyntaxException {
        Page<CoffeeOrder> page = coffeeOrderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coffeeOrders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coffeeOrders/:id -> get the "id" coffeeOrder.
     */
    @RequestMapping(value = "/coffeeOrders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoffeeOrder> getCoffeeOrder(@PathVariable Long id) {
        log.debug("REST request to get CoffeeOrder : {}", id);
        return Optional.ofNullable(coffeeOrderRepository.findOne(id))
            .map(coffeeOrder -> new ResponseEntity<>(
                coffeeOrder,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coffeeOrders/:id -> delete the "id" coffeeOrder.
     */
//    @RequestMapping(value = "/coffeeOrders/{id}",
//        method = RequestMethod.DELETE,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<Void> deleteCoffeeOrder(@PathVariable Long id) {
//        log.debug("REST request to delete CoffeeOrder : {}", id);
//        coffeeOrderRepository.delete(id);
//        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coffeeOrder", id.toString())).build();
//    }
}
